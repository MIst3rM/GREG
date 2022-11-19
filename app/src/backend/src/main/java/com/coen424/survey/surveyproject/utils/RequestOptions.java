package com.coen424.survey.surveyproject.utils;

public class RequestOptions {
    private final Table table_type;
    private final String partition_key;
    private final String sort_key;

    public String getPartition_key() {
        return partition_key;
    }
    public String getSort_key() {
        return sort_key;
    }
    public Table getTable_type() {
        return table_type;
    }
    private RequestOptions(RequestBuilder builder) {
        this.partition_key = builder.partition_key;
        this.sort_key = builder.sort_key;
        this.table_type = builder.table_type;
    }

    public static class RequestBuilder {
        private String partition_key;
        private String sort_key;
        private Table table_type;

        public RequestBuilder setPartition_key(String partition_key) {
            this.partition_key = partition_key;
            return this;
        }

        public RequestBuilder setSort_key(String sort_key) {
            this.sort_key = sort_key;
            return this;
        }

        public RequestBuilder setTable_type(Table table_type) {
            this.table_type = table_type;
            return this;
        }

        public RequestOptions build() {
            return new RequestOptions(this);
        }
    }
}
