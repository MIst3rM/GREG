package com.coen424.survey.surveyproject.utils;

import com.coen424.survey.surveyproject.models.Form_Submissions;
import com.coen424.survey.surveyproject.models.Forms;
import com.coen424.survey.surveyproject.models.SharedResults;

public enum Table {
    FORMS("Forms", Forms.class),
    FORM_SUBMISSIONS("Form_Submissions", Form_Submissions.class),
    SHARED_RESULTS("SharedResults", SharedResults.class),
    FORM_ATTACHMENTS("Form_Attachments", null);

    private final String tableName;
    private final Class<?> tableClass;

    Table(String tableName, Class<?> tableClass) {
        this.tableName = tableName;
        this.tableClass = tableClass;
    }

    public String getTableName() {
        return tableName;
    }

    public Class<?> getTableClass() {
        return tableClass;
    }
}
