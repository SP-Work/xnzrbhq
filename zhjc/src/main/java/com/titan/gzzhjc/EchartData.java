package com.titan.gzzhjc;

import com.github.abel533.echarts.Tooltip;
import com.github.abel533.echarts.data.Data;

/**
 * Created by whs on 2017/3/11
 */

public class EchartData extends Data {
    public String[] getCoord() {
        return coord;
    }

    public void setCoord(String[] coord) {
        this.coord = coord;
    }

    private String[] coord;

    public EchartData() {
        super();
    }

    public EchartData(String name) {
        super(name);
    }

    public EchartData(String name, Object value) {
        super(name, value);
    }

    public EchartData(String name, Object symbol, Object symbolSize) {
        super(name, symbol, symbolSize);
    }

    public EchartData(Object value, Object symbol) {
        super(value, symbol);
    }

    public EchartData(Object value, Object symbol, Object symbolSize) {
        super(value, symbol, symbolSize);
    }

    @Override
    public Object symbolSize() {
        return super.symbolSize();
    }

    @Override
    public Double smoothRadian() {
        return super.smoothRadian();
    }

    @Override
    public Data smoothRadian(Double smoothRadian) {
        return super.smoothRadian(smoothRadian);
    }

    @Override
    public Tooltip tooltip() {
        return super.tooltip();
    }

    @Override
    public Data tooltip(Tooltip tooltip) {
        return super.tooltip(tooltip);
    }

    @Override
    public Boolean selected() {
        return super.selected();
    }

    @Override
    public Data selected(Boolean selected) {
        return super.selected(selected);
    }

    @Override
    public String icon() {
        return super.icon();
    }

    @Override
    public Data icon(String icon) {
        return super.icon(icon);
    }

    @Override
    public Object min() {
        return super.min();
    }

    @Override
    public Data min(Object min) {
        return super.min(min);
    }

    @Override
    public Object max() {
        return super.max();
    }

    @Override
    public Data max(Object max) {
        return super.max(max);
    }

    @Override
    public Integer valueIndex() {
        return super.valueIndex();
    }

    @Override
    public Data valueIndex(Integer valueIndex) {
        return super.valueIndex(valueIndex);
    }

    @Override
    public Integer getValueIndex() {
        return super.getValueIndex();
    }

    @Override
    public Data setValueIndex(Integer valueIndex) {
        return super.setValueIndex(valueIndex);
    }

    @Override
    public Object getMin() {
        return super.getMin();
    }

    @Override
    public void setMin(Object min) {
        super.setMin(min);
    }

    @Override
    public Object getMax() {
        return super.getMax();
    }

    @Override
    public void setMax(Object max) {
        super.setMax(max);
    }

    @Override
    public String getIcon() {
        return super.getIcon();
    }

    @Override
    public void setIcon(String icon) {
        super.setIcon(icon);
    }

    @Override
    public Boolean getSelected() {
        return super.getSelected();
    }

    @Override
    public void setSelected(Boolean selected) {
        super.setSelected(selected);
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
    public Double getSmoothRadian() {
        return super.getSmoothRadian();
    }

    @Override
    public void setSmoothRadian(Double smoothRadian) {
        super.setSmoothRadian(smoothRadian);
    }
}
