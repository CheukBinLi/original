package com.cheuks.bin.original.db.manager;

public interface HibernateReplicationMode {
	final String EXCEPTION = "EXCEPTION";
	final String IGNORE = "IGNORE";
	final String OVERWRITE = "OVERWRITE";
	final String LATEST_VERSION = "LATEST_VERSION";
}
