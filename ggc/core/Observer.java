package ggc.core;

public interface Observer {
	void update(Notification notification);
	void clear();
}