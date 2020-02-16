package com.wk.entity.fcaps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AmCollectionSourceMonitorExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AmCollectionSourceMonitorExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andSourceIdIsNull() {
            addCriterion("source_id is null");
            return (Criteria) this;
        }

        public Criteria andSourceIdIsNotNull() {
            addCriterion("source_id is not null");
            return (Criteria) this;
        }

        public Criteria andSourceIdEqualTo(String value) {
            addCriterion("source_id =", value, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdNotEqualTo(String value) {
            addCriterion("source_id <>", value, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdGreaterThan(String value) {
            addCriterion("source_id >", value, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdGreaterThanOrEqualTo(String value) {
            addCriterion("source_id >=", value, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdLessThan(String value) {
            addCriterion("source_id <", value, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdLessThanOrEqualTo(String value) {
            addCriterion("source_id <=", value, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdLike(String value) {
            addCriterion("source_id like", value, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdNotLike(String value) {
            addCriterion("source_id not like", value, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdIn(List<String> values) {
            addCriterion("source_id in", values, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdNotIn(List<String> values) {
            addCriterion("source_id not in", values, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdBetween(String value1, String value2) {
            addCriterion("source_id between", value1, value2, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdNotBetween(String value1, String value2) {
            addCriterion("source_id not between", value1, value2, "sourceId");
            return (Criteria) this;
        }

        public Criteria andInfoTypeIsNull() {
            addCriterion("info_type is null");
            return (Criteria) this;
        }

        public Criteria andInfoTypeIsNotNull() {
            addCriterion("info_type is not null");
            return (Criteria) this;
        }

        public Criteria andInfoTypeEqualTo(String value) {
            addCriterion("info_type =", value, "infoType");
            return (Criteria) this;
        }

        public Criteria andInfoTypeNotEqualTo(String value) {
            addCriterion("info_type <>", value, "infoType");
            return (Criteria) this;
        }

        public Criteria andInfoTypeGreaterThan(String value) {
            addCriterion("info_type >", value, "infoType");
            return (Criteria) this;
        }

        public Criteria andInfoTypeGreaterThanOrEqualTo(String value) {
            addCriterion("info_type >=", value, "infoType");
            return (Criteria) this;
        }

        public Criteria andInfoTypeLessThan(String value) {
            addCriterion("info_type <", value, "infoType");
            return (Criteria) this;
        }

        public Criteria andInfoTypeLessThanOrEqualTo(String value) {
            addCriterion("info_type <=", value, "infoType");
            return (Criteria) this;
        }

        public Criteria andInfoTypeLike(String value) {
            addCriterion("info_type like", value, "infoType");
            return (Criteria) this;
        }

        public Criteria andInfoTypeNotLike(String value) {
            addCriterion("info_type not like", value, "infoType");
            return (Criteria) this;
        }

        public Criteria andInfoTypeIn(List<String> values) {
            addCriterion("info_type in", values, "infoType");
            return (Criteria) this;
        }

        public Criteria andInfoTypeNotIn(List<String> values) {
            addCriterion("info_type not in", values, "infoType");
            return (Criteria) this;
        }

        public Criteria andInfoTypeBetween(String value1, String value2) {
            addCriterion("info_type between", value1, value2, "infoType");
            return (Criteria) this;
        }

        public Criteria andInfoTypeNotBetween(String value1, String value2) {
            addCriterion("info_type not between", value1, value2, "infoType");
            return (Criteria) this;
        }

        public Criteria andCheckPointIsNull() {
            addCriterion("check_point is null");
            return (Criteria) this;
        }

        public Criteria andCheckPointIsNotNull() {
            addCriterion("check_point is not null");
            return (Criteria) this;
        }

        public Criteria andCheckPointEqualTo(Date value) {
            addCriterion("check_point =", value, "checkPoint");
            return (Criteria) this;
        }

        public Criteria andCheckPointNotEqualTo(Date value) {
            addCriterion("check_point <>", value, "checkPoint");
            return (Criteria) this;
        }

        public Criteria andCheckPointGreaterThan(Date value) {
            addCriterion("check_point >", value, "checkPoint");
            return (Criteria) this;
        }

        public Criteria andCheckPointGreaterThanOrEqualTo(Date value) {
            addCriterion("check_point >=", value, "checkPoint");
            return (Criteria) this;
        }

        public Criteria andCheckPointLessThan(Date value) {
            addCriterion("check_point <", value, "checkPoint");
            return (Criteria) this;
        }

        public Criteria andCheckPointLessThanOrEqualTo(Date value) {
            addCriterion("check_point <=", value, "checkPoint");
            return (Criteria) this;
        }

        public Criteria andCheckPointIn(List<Date> values) {
            addCriterion("check_point in", values, "checkPoint");
            return (Criteria) this;
        }

        public Criteria andCheckPointNotIn(List<Date> values) {
            addCriterion("check_point not in", values, "checkPoint");
            return (Criteria) this;
        }

        public Criteria andCheckPointBetween(Date value1, Date value2) {
            addCriterion("check_point between", value1, value2, "checkPoint");
            return (Criteria) this;
        }

        public Criteria andCheckPointNotBetween(Date value1, Date value2) {
            addCriterion("check_point not between", value1, value2, "checkPoint");
            return (Criteria) this;
        }

        public Criteria andAlarmSeqIsNull() {
            addCriterion("alarm_seq is null");
            return (Criteria) this;
        }

        public Criteria andAlarmSeqIsNotNull() {
            addCriterion("alarm_seq is not null");
            return (Criteria) this;
        }

        public Criteria andAlarmSeqEqualTo(Integer value) {
            addCriterion("alarm_seq =", value, "alarmSeq");
            return (Criteria) this;
        }

        public Criteria andAlarmSeqNotEqualTo(Integer value) {
            addCriterion("alarm_seq <>", value, "alarmSeq");
            return (Criteria) this;
        }

        public Criteria andAlarmSeqGreaterThan(Integer value) {
            addCriterion("alarm_seq >", value, "alarmSeq");
            return (Criteria) this;
        }

        public Criteria andAlarmSeqGreaterThanOrEqualTo(Integer value) {
            addCriterion("alarm_seq >=", value, "alarmSeq");
            return (Criteria) this;
        }

        public Criteria andAlarmSeqLessThan(Integer value) {
            addCriterion("alarm_seq <", value, "alarmSeq");
            return (Criteria) this;
        }

        public Criteria andAlarmSeqLessThanOrEqualTo(Integer value) {
            addCriterion("alarm_seq <=", value, "alarmSeq");
            return (Criteria) this;
        }

        public Criteria andAlarmSeqIn(List<Integer> values) {
            addCriterion("alarm_seq in", values, "alarmSeq");
            return (Criteria) this;
        }

        public Criteria andAlarmSeqNotIn(List<Integer> values) {
            addCriterion("alarm_seq not in", values, "alarmSeq");
            return (Criteria) this;
        }

        public Criteria andAlarmSeqBetween(Integer value1, Integer value2) {
            addCriterion("alarm_seq between", value1, value2, "alarmSeq");
            return (Criteria) this;
        }

        public Criteria andAlarmSeqNotBetween(Integer value1, Integer value2) {
            addCriterion("alarm_seq not between", value1, value2, "alarmSeq");
            return (Criteria) this;
        }

        public Criteria andHeartbeatTimeIsNull() {
            addCriterion("heartbeat_time is null");
            return (Criteria) this;
        }

        public Criteria andHeartbeatTimeIsNotNull() {
            addCriterion("heartbeat_time is not null");
            return (Criteria) this;
        }

        public Criteria andHeartbeatTimeEqualTo(Date value) {
            addCriterion("heartbeat_time =", value, "heartbeatTime");
            return (Criteria) this;
        }

        public Criteria andHeartbeatTimeNotEqualTo(Date value) {
            addCriterion("heartbeat_time <>", value, "heartbeatTime");
            return (Criteria) this;
        }

        public Criteria andHeartbeatTimeGreaterThan(Date value) {
            addCriterion("heartbeat_time >", value, "heartbeatTime");
            return (Criteria) this;
        }

        public Criteria andHeartbeatTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("heartbeat_time >=", value, "heartbeatTime");
            return (Criteria) this;
        }

        public Criteria andHeartbeatTimeLessThan(Date value) {
            addCriterion("heartbeat_time <", value, "heartbeatTime");
            return (Criteria) this;
        }

        public Criteria andHeartbeatTimeLessThanOrEqualTo(Date value) {
            addCriterion("heartbeat_time <=", value, "heartbeatTime");
            return (Criteria) this;
        }

        public Criteria andHeartbeatTimeIn(List<Date> values) {
            addCriterion("heartbeat_time in", values, "heartbeatTime");
            return (Criteria) this;
        }

        public Criteria andHeartbeatTimeNotIn(List<Date> values) {
            addCriterion("heartbeat_time not in", values, "heartbeatTime");
            return (Criteria) this;
        }

        public Criteria andHeartbeatTimeBetween(Date value1, Date value2) {
            addCriterion("heartbeat_time between", value1, value2, "heartbeatTime");
            return (Criteria) this;
        }

        public Criteria andHeartbeatTimeNotBetween(Date value1, Date value2) {
            addCriterion("heartbeat_time not between", value1, value2, "heartbeatTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}