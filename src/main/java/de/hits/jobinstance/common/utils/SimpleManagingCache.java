package de.hits.jobinstance.common.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * https://crunchify.com/hashmap-vs-concurrenthashmap-vs-synchronizedmap-how-a-hashmap-can-be-synchronized-in-java/<br>
 * https://crunchify.com/implement-simple-threadsafe-cache-using-hashmap-without-using-synchronized-collection/<br>
 * https://crunchify.com/how-to-create-a-simple-in-memory-cache-in-java-lightweight-cache/<br>
 * 
 * @author André Hermann
 * @version 1.0
 * @since 13.02.2018
 * 
 * @param <K>
 *            the type of keys maintained by this map
 * @param <V>
 *            the type of mapped values
 */
public class SimpleManagingCache<K, V> {

	private Logger log = null;

	private boolean loggingEnabled = false;
	private boolean monitoringEnabled = false;

	private long timeToLive;

	private ConcurrentHashMap<K, CacheObject> cacheMap;

	private final AtomicLong inserted = new AtomicLong(0);
	private final AtomicLong requested = new AtomicLong(0);
	private final AtomicLong removed = new AtomicLong(0);
	private final AtomicLong killed = new AtomicLong(0);

	protected class CacheObject {
		public final long created = System.currentTimeMillis();
		public V value;

		protected CacheObject(V value) {
			this.value = value;
		}
	}

	/**
	 * 
	 * @param timeToLive
	 *            maximum number of seconds, an object have to be in the cache
	 * @param managingTimerInterval
	 *            the interval in seconds, how often the cache management has to run
	 */
	public SimpleManagingCache(long timeToLive, final long managingTimerInterval) {
		int initialCapacity = 100000;
		final long multiplierForMillis = 1000l;

		this.timeToLive = timeToLive * multiplierForMillis;
		this.cacheMap = new ConcurrentHashMap<>(initialCapacity);

		if (timeToLive > 0 && managingTimerInterval > 0) {
			Thread thread = new Thread(new Runnable() {
				public void run() {
					while (true) {
						try {
							Thread.sleep(managingTimerInterval * multiplierForMillis);
						} catch (InterruptedException ex) {
						}

						cleanup();
					}
				}
			});

			thread.setDaemon(true);
			thread.start();
		}
	}

	public void put(K key, V value) {
		if (loggingEnabled && log.isTraceEnabled()) {
			log.trace(getClass().getSimpleName() + "#put()");
		}

		inserted.incrementAndGet();

		cacheMap.put(key, new CacheObject(value));
	}

	public V get(K key) {
		if (loggingEnabled && log.isTraceEnabled()) {
			log.trace(getClass().getSimpleName() + "#get()");
		}

		requested.incrementAndGet();

		CacheObject cacheObject = cacheMap.get(key);

		if (cacheObject == null)
			return null;
		else {
			return cacheObject.value;
		}
	}

	public Map<K, V> getEntries() {
		Map<K, V> tempMap = new HashMap<>();
		this.cacheMap.entrySet().forEach(e -> tempMap.put(e.getKey(), e.getValue().value));

		return tempMap;
	}

	public void remove(K key) {
		if (loggingEnabled && log.isTraceEnabled()) {
			log.trace(getClass().getSimpleName() + "#remove()");
		}

		removed.incrementAndGet();

		cacheMap.remove(key);
	}

	public void removeList(List<K> toRemove) {
		if (loggingEnabled && log.isTraceEnabled()) {
			log.trace(getClass().getSimpleName() + "#removeList()");
		}

		toRemove.forEach(key -> {
			removed.incrementAndGet();
			cacheMap.remove(key);
		});
	}

	public boolean containsKey(K key) {
		if (loggingEnabled && log.isTraceEnabled()) {
			log.trace(getClass().getSimpleName() + "#containsKey()");
		}

		return cacheMap.containsKey(key);
	}

	public int size() {
		if (loggingEnabled && log.isTraceEnabled()) {
			log.trace(getClass().getSimpleName() + "#size()");
		}

		return cacheMap.size();
	}

	private void cleanup() {
		if (loggingEnabled && log.isTraceEnabled()) {
			log.trace(getClass().getSimpleName() + "#cleanup()");
		}

		long thresholdAge = System.currentTimeMillis() - timeToLive;
		int parallelismThreshold = 5;

		AtomicLong deleteCounter = new AtomicLong(0);
		long cleanStart = System.nanoTime();
		int sizeBeforCleansing = cacheMap.size();

		cacheMap.forEach(parallelismThreshold, (k, v) -> {
			if (v.created < thresholdAge) {
				deleteCounter.incrementAndGet();
				killed.incrementAndGet();
				cacheMap.remove(k);
			}
		});

		if (loggingEnabled) {
			long cleanEnd = System.nanoTime();
			long totalTime = (cleanEnd - cleanStart) / 1000000L;

			StringBuilder msg = new StringBuilder();
			msg.append("Cleansing:\n");
			msg.append(String.format("  Cache size befor cleaning: %s\n", sizeBeforCleansing));
			msg.append(String.format("  Cleaned entries:           %s (sum: %s)\n", deleteCounter.get(), killed.get()));
			msg.append(String.format("  Cache size after cleaning: %s\n", cacheMap.size()));
			msg.append(String.format("  Cache cleaned in %s ms", totalTime));

			log.info(msg.toString());
		}
	}

	private void runMonitoring(final long monitoringTimerInterval) {
		if (loggingEnabled && log.isTraceEnabled()) {
			log.trace(getClass().getSimpleName() + "#runMonitoring()");
		}

		Thread thread = new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(monitoringTimerInterval * 1000l);
					} catch (InterruptedException ex) {
					}

					monitor();
				}
			}
		});

		thread.setDaemon(true);
		thread.start();
	}

	private void monitor() {
		if (loggingEnabled && log.isTraceEnabled()) {
			log.trace(getClass().getSimpleName() + "#monitor()");
		}

		final long divisorForMinutes = 60000l;

		final long insertedCount = inserted.get();
		final long removedCount = removed.get();
		final long requestedCount = requested.get();
		final long sumRequests = insertedCount + removedCount + requestedCount;
		final int cacheSize = cacheMap.size();
		final long threshold = System.currentTimeMillis();

		ConcurrentHashMap<Long, Long> reducedMap = new ConcurrentHashMap<>();
		cacheMap.forEachValue(1000000, (v) -> {
			long key = ((threshold - v.created) / divisorForMinutes);
			Long value = reducedMap.get(key);
			if (value == null) {
				reducedMap.put(key, 1l);
			} else {
				reducedMap.compute(key, (k, v2) -> (v2 + 1));
			}
		});

		StringBuilder msg = new StringBuilder();
		msg.append("Monitoring:\n");
		msg.append("  Requests:\n");
		msg.append(String.format("    Inserted entries:  %s\n", insertedCount));
		msg.append(String.format("    Removed entries:   %s\n", removedCount));
		msg.append(String.format("    Requested entries: %s\n", requestedCount));
		msg.append(String.format("    Actions summary:   %s\n", sumRequests));
		msg.append("  Cache health:\n");
		msg.append(String.format("    Actual cache size: %s\n", cacheSize));
		msg.append("   .=============================.\n");
		msg.append("   | Age in minutes | Count      |\n");
		msg.append("   .-----------------------------.\n");
		reducedMap.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEachOrdered(
				entry -> msg.append(String.format("   | %-14s | %-10s |\n", entry.getKey(), entry.getValue())));
		msg.append("   .=============================.");

		log.info(msg.toString());
	}

	/**
	 * Methode to activate logging, logging is by default inactive.
	 * 
	 * @param active
	 */
	public void setLogging(boolean active) {
		loggingEnabled = active;
		if (loggingEnabled && log == null) {
			log = LoggerFactory.getLogger(getClass());
		}

		if (loggingEnabled && log.isTraceEnabled()) {
			log.trace(getClass().getSimpleName() + "#setLogging()");
		}
	}

	/**
	 * Methode to activate monitoring, monitoring is by default inactive
	 * 
	 * @param active
	 * @param monitoringTimerInterval
	 *            the interval in seconds, how often the monitoring has to run
	 */
	public void setMonitoring(boolean active, final long monitoringTimerInterval) {
		if (loggingEnabled && log.isTraceEnabled()) {
			log.trace(getClass().getSimpleName() + "#setMonitoring()");
		}

		if (!monitoringEnabled && active && monitoringTimerInterval > 0) {
			runMonitoring(monitoringTimerInterval);
		}

		monitoringEnabled = active;
	}
}