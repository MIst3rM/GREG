package com.coen424.survey.surveyproject.utils;

import software.amazon.awssdk.enhanced.dynamodb.Expression;

import java.util.Arrays;

public class RequestOptions {
    private final Table table_type;
    private final String partition_key;
    private final String sort_key;
    private final Expression expression;
    private final String[] attributesToProject;

    public String getPartition_key() {
        return partition_key;
    }
    public String getSort_key() {
        return sort_key;
    }
    public Table getTable_type() {
        return table_type;
    }

    public Expression getExpression() {
        return expression;
    }
    public String[] getAttributesToProject() {
        return attributesToProject;
    }
    public boolean hasPartitionKey(){
        return partition_key != null;
    }
    public boolean hasSortKey(){
        return sort_key != null;
    }
    public boolean hasExpression(){
        return expression != null;
    }
    public boolean hasAttributesToProject(){
        return attributesToProject != null;
    }

    @Override
    public String toString() {
        return "RequestOptions{" +
                "table_type=" + table_type +
                ", partition_key='" + partition_key + '\'' +
                ", sort_key='" + sort_key + '\'' +
                ", expression=" + expression +
                ", attributesToProject='" + Arrays.toString(attributesToProject) + '\'' +
                '}';
    }

    private RequestOptions(RequestBuilder builder) {
        this.partition_key = builder.partition_key;
        this.sort_key = builder.sort_key;
        this.table_type = builder.table_type;
        this.expression = builder.expression;
        this.attributesToProject = builder.attributesToProject;
    }

    public static class RequestBuilder {
        private String partition_key;
        private String sort_key;
        private Table table_type;
        private Expression expression;

        private String[] attributesToProject;

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
        public RequestBuilder setExpression(Expression expression) {
            this.expression = expression;
            return this;
        }

        public RequestBuilder setAttributesToProject(String[] attributesToProject) {
            this.attributesToProject = attributesToProject;
            return this;
        }

        public RequestOptions build() {
            return new RequestOptions(this);
        }
    }
}
