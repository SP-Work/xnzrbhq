package com.titan.gzzhjc;

import com.github.abel533.echarts.Tooltip;
import com.github.abel533.echarts.code.CoordinateSystem;
import com.github.abel533.echarts.code.SelectedMode;
import com.github.abel533.echarts.code.SeriesType;
import com.github.abel533.echarts.code.Symbol;
import com.github.abel533.echarts.code.X;
import com.github.abel533.echarts.code.Y;
import com.github.abel533.echarts.series.MarkLine;
import com.github.abel533.echarts.series.MarkPoint;
import com.github.abel533.echarts.series.Series;
import com.github.abel533.echarts.style.ItemStyle;

/**
 * Created by whs on 2017/3/3
 */

public class MySeries extends Series {
    String mapType;

    public SelectedMode getSelectedMode() {
        return selectedMode;
    }

    public void setSelectedMode(SelectedMode selectedMode) {
        this.selectedMode = selectedMode;
    }

    SelectedMode selectedMode;

//    protected MySeries() {
//        super();
//    }

    public MySeries(){
        super();
    }

    protected MySeries(String name) {
        super(name);
    }

    /**
     * MapType
     */
    public void setMaptype(String maptype)
    {
        this.mapType=maptype;

    }

    @Override
    public Object left() {
        return super.left();
    }

    @Override
    public Object left(Object left) {
        return super.left(left);
    }

    @Override
    public Object left(Integer left) {
        return super.left(left);
    }

    @Override
    public Object left(X left) {
        return super.left(left);
    }

    @Override
    public Object top() {
        return super.top();
    }

    @Override
    public Object top(Object top) {
        return super.top(top);
    }

    @Override
    public Object top(Integer top) {
        return super.top(top);
    }

    @Override
    public Object top(Y top) {
        return super.top(top);
    }

    @Override
    public Object right() {
        return super.right();
    }

    @Override
    public Object right(Object right) {
        return super.right(right);
    }

    @Override
    public Object right(Integer right) {
        return super.right(right);
    }

    @Override
    public Object bottom() {
        return super.bottom();
    }

    @Override
    public Object bottom(Object bottom) {
        return super.bottom(bottom);
    }

    @Override
    public Object bottom(Integer bottom) {
        return super.bottom(bottom);
    }

    @Override
    public Object width() {
        return super.width();
    }

    @Override
    public Object width(Object width) {
        return super.width(width);
    }

    @Override
    public Object width(Integer width) {
        return super.width(width);
    }

    @Override
    public Object height() {
        return super.height();
    }

    @Override
    public Object height(Object height) {
        return super.height(height);
    }

    @Override
    public Object height(Integer height) {
        return super.height(height);
    }

    @Override
    public Object[] symbolOffset() {
        return super.symbolOffset();
    }

    @Override
    public Object symbolOffset(Object[] symbolOffset) {
        return super.symbolOffset(symbolOffset);
    }

    @Override
    public Object symbolOffset(Object o1, Object o2) {
        return super.symbolOffset(o1, o2);
    }

    @Override
    public Object[] getSymbolOffset() {
        return super.getSymbolOffset();
    }

    @Override
    public void setSymbolOffset(Object[] symbolOffset) {
        super.setSymbolOffset(symbolOffset);
    }

    @Override
    public Object coordinateSystem() {
        return super.coordinateSystem();
    }

    @Override
    public Object coordinateSystem(Object coordinateSystem) {
        return super.coordinateSystem(coordinateSystem);
    }

    @Override
    public Object coordinateSystem(CoordinateSystem coordinateSystem) {
        return super.coordinateSystem(coordinateSystem);
    }

    @Override
    public Object getCoordinateSystem() {
        return super.getCoordinateSystem();
    }

    @Override
    public void setCoordinateSystem(Object coordinateSystem) {
        super.setCoordinateSystem(coordinateSystem);
    }

    @Override
    public ItemStyle getLabel() {
        return super.getLabel();
    }

    @Override
    public void setLabel(ItemStyle label) {
        super.setLabel(label);
    }

    @Override
    public Object label(ItemStyle label) {
        return super.label(label);
    }

    @Override
    public ItemStyle label() {
        return super.label();
    }

    @Override
    public Object zlevel(Integer zlevel) {
        return super.zlevel(zlevel);
    }

    @Override
    public Integer zlevel() {
        return super.zlevel();
    }

    @Override
    public Object z(Integer z) {
        return super.z(z);
    }

    @Override
    public Integer z() {
        return super.z();
    }

    @Override
    public Series tooltip(Tooltip tooltip) {
        return super.tooltip(tooltip);
    }

    @Override
    public Series itemStyle(ItemStyle itemStyle) {
        return super.itemStyle(itemStyle);
    }

    @Override
    public Series markPoint(MarkPoint markPoint) {
        return super.markPoint(markPoint);
    }

    @Override
    public Series markLine(MarkLine markLine) {
        return super.markLine(markLine);
    }

    @Override
    public Boolean legendHoverLink() {
        return super.legendHoverLink();
    }

    @Override
    public Object legendHoverLink(Boolean legendHoverLink) {
        return super.legendHoverLink(legendHoverLink);
    }

    @Override
    public Integer xAxisIndex() {
        return super.xAxisIndex();
    }

    @Override
    public Object xAxisIndex(Integer xAxisIndex) {
        return super.xAxisIndex(xAxisIndex);
    }

    @Override
    public Integer yAxisIndex() {
        return super.yAxisIndex();
    }

    @Override
    public Object yAxisIndex(Integer yAxisIndex) {
        return super.yAxisIndex(yAxisIndex);
    }

    @Override
    public Integer geoIndex() {
        return super.geoIndex();
    }

    @Override
    public Object geoIndex(Integer geoIndex) {
        return super.geoIndex(geoIndex);
    }

    @Override
    public String name() {
        return super.name();
    }

    @Override
    public Object name(String name) {
        return super.name(name);
    }

    @Override
    public SeriesType type() {
        return super.type();
    }

    @Override
    public Object type(SeriesType type) {
        return super.type(type);
    }

    @Override
    public String stack() {
        return super.stack();
    }

    @Override
    public Object stack(String stack) {
        return super.stack(stack);
    }

    @Override
    public Tooltip tooltip() {
        return super.tooltip();
    }

    @Override
    public ItemStyle itemStyle() {
        return super.itemStyle();
    }

    @Override
    public MarkPoint markPoint() {
        return super.markPoint();
    }

    @Override
    public MarkLine markLine() {
        return super.markLine();
    }

    @Override
    public Object symbol() {
        return super.symbol();
    }

    @Override
    public Object symbol(Object symbol) {
        return super.symbol(symbol);
    }

    @Override
    public Object symbol(Symbol symbol) {
        return super.symbol(symbol);
    }

    @Override
    public Object symbolSize() {
        return super.symbolSize();
    }

    @Override
    public Object symbolSize(Object symbolSize) {
        return super.symbolSize(symbolSize);
    }

    @Override
    public Object symbolRoate() {
        return super.symbolRoate();
    }

    @Override
    public Object symbolRoate(Object symbolRoate) {
        return super.symbolRoate(symbolRoate);
    }

    @Override
    public Boolean showAllSymbol() {
        return super.showAllSymbol();
    }

    @Override
    public Object calcuable(Boolean calculable) {
        return super.calcuable(calculable);
    }

    @Override
    public Boolean calculable() {
        return super.calculable();
    }

    @Override
    public Object showAllSymbol(Boolean showAllSymbol) {
        return super.showAllSymbol(showAllSymbol);
    }

    @Override
    public Boolean getLegendHoverLink() {
        return super.getLegendHoverLink();
    }

    @Override
    public void setLegendHoverLink(Boolean legendHoverLink) {
        super.setLegendHoverLink(legendHoverLink);
    }

    @Override
    public Tooltip getTooltip() {
        return super.getTooltip();
    }

    @Override
    public void setTooltip(Tooltip tooltip) {
        super.setTooltip(tooltip);
    }

    @Override
    public ItemStyle getItemStyle() {
        return super.getItemStyle();
    }

    @Override
    public void setItemStyle(ItemStyle itemStyle) {
        super.setItemStyle(itemStyle);
    }

    @Override
    public MarkPoint getMarkPoint() {
        return super.getMarkPoint();
    }

    @Override
    public void setMarkPoint(MarkPoint markPoint) {
        super.setMarkPoint(markPoint);
    }

    @Override
    public MarkLine getMarkLine() {
        return super.getMarkLine();
    }

    @Override
    public void setMarkLine(MarkLine markLine) {
        super.setMarkLine(markLine);
    }

    @Override
    public Integer getxAxisIndex() {
        return super.getxAxisIndex();
    }

    @Override
    public void setxAxisIndex(Integer xAxisIndex) {
        super.setxAxisIndex(xAxisIndex);
    }

    @Override
    public Integer getyAxisIndex() {
        return super.getyAxisIndex();
    }

    @Override
    public void setyAxisIndex(Integer yAxisIndex) {
        super.setyAxisIndex(yAxisIndex);
    }

    @Override
    public Integer getGeoIndex() {
        return super.getGeoIndex();
    }

    @Override
    public void setGeoIndex(Integer geoIndex) {
        super.setGeoIndex(geoIndex);
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public SeriesType getType() {
        return super.getType();
    }

    @Override
    public void setType(SeriesType type) {
        super.setType(type);
    }

    @Override
    public String getStack() {
        return super.getStack();
    }

    @Override
    public void setStack(String stack) {
        super.setStack(stack);
    }

    @Override
    public Object getSymbol() {
        return super.getSymbol();
    }

    @Override
    public void setSymbol(Object symbol) {
        super.setSymbol(symbol);
    }

    @Override
    public Object getSymbolSize() {
        return super.getSymbolSize();
    }

    @Override
    public void setSymbolSize(Object symbolSize) {
        super.setSymbolSize(symbolSize);
    }

    @Override
    public Object getSymbolRoate() {
        return super.getSymbolRoate();
    }

    @Override
    public void setSymbolRoate(Object symbolRoate) {
        super.setSymbolRoate(symbolRoate);
    }

    @Override
    public Boolean getShowAllSymbol() {
        return super.getShowAllSymbol();
    }

    @Override
    public void setShowAllSymbol(Boolean showAllSymbol) {
        super.setShowAllSymbol(showAllSymbol);
    }

    @Override
    public Boolean getCalculable() {
        return super.getCalculable();
    }

    @Override
    public void setCalculable(Boolean calculable) {
        super.setCalculable(calculable);
    }

    @Override
    public Integer getZlevel() {
        return super.getZlevel();
    }

    @Override
    public void setZlevel(Integer zlevel) {
        super.setZlevel(zlevel);
    }

    @Override
    public Integer getZ() {
        return super.getZ();
    }

    @Override
    public void setZ(Integer z) {
        super.setZ(z);
    }

    @Override
    public Object getLeft() {
        return super.getLeft();
    }

    @Override
    public void setLeft(Object left) {
        super.setLeft(left);
    }

    @Override
    public Object getTop() {
        return super.getTop();
    }

    @Override
    public void setTop(Object top) {
        super.setTop(top);
    }

    @Override
    public Object getRight() {
        return super.getRight();
    }

    @Override
    public void setRight(Object right) {
        super.setRight(right);
    }

    @Override
    public Object getBottom() {
        return super.getBottom();
    }

    @Override
    public void setBottom(Object bottom) {
        super.setBottom(bottom);
    }

    @Override
    public Object getWidth() {
        return super.getWidth();
    }

    @Override
    public void setWidth(Object width) {
        super.setWidth(width);
    }

    @Override
    public Object getHeight() {
        return super.getHeight();
    }

    @Override
    public void setHeight(Object height) {
        super.setHeight(height);
    }
}
