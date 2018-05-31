package com.otitan.xnbhq.drawTool;

import java.util.EventListener;

/**
 * 事件监听
 */
public interface DrawEventListener extends EventListener {

	void handleDrawEvent(DrawEvent event);
}
