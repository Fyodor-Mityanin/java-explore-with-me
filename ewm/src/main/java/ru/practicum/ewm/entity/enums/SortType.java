package ru.practicum.ewm.entity.enums;

public enum SortType {
    EVENT_DATE("event_date"),
    VIEWS("views");

    private final String columnName;

    SortType(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }
}
