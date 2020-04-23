package com.wk.entity.fcaps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AmCollectorHostInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AmCollectorHostInfoExample() {
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

        public Criteria andCollectorIdIsNull() {
            addCriterion("collector_id is null");
            return (Criteria) this;
        }

        public Criteria andCollectorIdIsNotNull() {
            addCriterion("collector_id is not null");
            return (Criteria) this;
        }

        public Criteria andCollectorIdEqualTo(Integer value) {
            addCriterion("collector_id =", value, "collectorId");
            return (Criteria) this;
        }

        public Criteria andCollectorIdNotEqualTo(Integer value) {
            addCriterion("collector_id <>", value, "collectorId");
            return (Criteria) this;
        }

        public Criteria andCollectorIdGreaterThan(Integer value) {
            addCriterion("collector_id >", value, "collectorId");
            return (Criteria) this;
        }

        public Criteria andCollectorIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("collector_id >=", value, "collectorId");
            return (Criteria) this;
        }

        public Criteria andCollectorIdLessThan(Integer value) {
            addCriterion("collector_id <", value, "collectorId");
            return (Criteria) this;
        }

        public Criteria andCollectorIdLessThanOrEqualTo(Integer value) {
            addCriterion("collector_id <=", value, "collectorId");
            return (Criteria) this;
        }

        public Criteria andCollectorIdIn(List<Integer> values) {
            addCriterion("collector_id in", values, "collectorId");
            return (Criteria) this;
        }

        public Criteria andCollectorIdNotIn(List<Integer> values) {
            addCriterion("collector_id not in", values, "collectorId");
            return (Criteria) this;
        }

        public Criteria andCollectorIdBetween(Integer value1, Integer value2) {
            addCriterion("collector_id between", value1, value2, "collectorId");
            return (Criteria) this;
        }

        public Criteria andCollectorIdNotBetween(Integer value1, Integer value2) {
            addCriterion("collector_id not between", value1, value2, "collectorId");
            return (Criteria) this;
        }

        public Criteria andCollectorNameIsNull() {
            addCriterion("collector_name is null");
            return (Criteria) this;
        }

        public Criteria andCollectorNameIsNotNull() {
            addCriterion("collector_name is not null");
            return (Criteria) this;
        }

        public Criteria andCollectorNameEqualTo(String value) {
            addCriterion("collector_name =", value, "collectorName");
            return (Criteria) this;
        }

        public Criteria andCollectorNameNotEqualTo(String value) {
            addCriterion("collector_name <>", value, "collectorName");
            return (Criteria) this;
        }

        public Criteria andCollectorNameGreaterThan(String value) {
            addCriterion("collector_name >", value, "collectorName");
            return (Criteria) this;
        }

        public Criteria andCollectorNameGreaterThanOrEqualTo(String value) {
            addCriterion("collector_name >=", value, "collectorName");
            return (Criteria) this;
        }

        public Criteria andCollectorNameLessThan(String value) {
            addCriterion("collector_name <", value, "collectorName");
            return (Criteria) this;
        }

        public Criteria andCollectorNameLessThanOrEqualTo(String value) {
            addCriterion("collector_name <=", value, "collectorName");
            return (Criteria) this;
        }

        public Criteria andCollectorNameLike(String value) {
            addCriterion("collector_name like", value, "collectorName");
            return (Criteria) this;
        }

        public Criteria andCollectorNameNotLike(String value) {
            addCriterion("collector_name not like", value, "collectorName");
            return (Criteria) this;
        }

        public Criteria andCollectorNameIn(List<String> values) {
            addCriterion("collector_name in", values, "collectorName");
            return (Criteria) this;
        }

        public Criteria andCollectorNameNotIn(List<String> values) {
            addCriterion("collector_name not in", values, "collectorName");
            return (Criteria) this;
        }

        public Criteria andCollectorNameBetween(String value1, String value2) {
            addCriterion("collector_name between", value1, value2, "collectorName");
            return (Criteria) this;
        }

        public Criteria andCollectorNameNotBetween(String value1, String value2) {
            addCriterion("collector_name not between", value1, value2, "collectorName");
            return (Criteria) this;
        }

        public Criteria andCollectorInnerIpIsNull() {
            addCriterion("collector_inner_ip is null");
            return (Criteria) this;
        }

        public Criteria andCollectorInnerIpIsNotNull() {
            addCriterion("collector_inner_ip is not null");
            return (Criteria) this;
        }

        public Criteria andCollectorInnerIpEqualTo(String value) {
            addCriterion("collector_inner_ip =", value, "collectorInnerIp");
            return (Criteria) this;
        }

        public Criteria andCollectorInnerIpNotEqualTo(String value) {
            addCriterion("collector_inner_ip <>", value, "collectorInnerIp");
            return (Criteria) this;
        }

        public Criteria andCollectorInnerIpGreaterThan(String value) {
            addCriterion("collector_inner_ip >", value, "collectorInnerIp");
            return (Criteria) this;
        }

        public Criteria andCollectorInnerIpGreaterThanOrEqualTo(String value) {
            addCriterion("collector_inner_ip >=", value, "collectorInnerIp");
            return (Criteria) this;
        }

        public Criteria andCollectorInnerIpLessThan(String value) {
            addCriterion("collector_inner_ip <", value, "collectorInnerIp");
            return (Criteria) this;
        }

        public Criteria andCollectorInnerIpLessThanOrEqualTo(String value) {
            addCriterion("collector_inner_ip <=", value, "collectorInnerIp");
            return (Criteria) this;
        }

        public Criteria andCollectorInnerIpLike(String value) {
            addCriterion("collector_inner_ip like", value, "collectorInnerIp");
            return (Criteria) this;
        }

        public Criteria andCollectorInnerIpNotLike(String value) {
            addCriterion("collector_inner_ip not like", value, "collectorInnerIp");
            return (Criteria) this;
        }

        public Criteria andCollectorInnerIpIn(List<String> values) {
            addCriterion("collector_inner_ip in", values, "collectorInnerIp");
            return (Criteria) this;
        }

        public Criteria andCollectorInnerIpNotIn(List<String> values) {
            addCriterion("collector_inner_ip not in", values, "collectorInnerIp");
            return (Criteria) this;
        }

        public Criteria andCollectorInnerIpBetween(String value1, String value2) {
            addCriterion("collector_inner_ip between", value1, value2, "collectorInnerIp");
            return (Criteria) this;
        }

        public Criteria andCollectorInnerIpNotBetween(String value1, String value2) {
            addCriterion("collector_inner_ip not between", value1, value2, "collectorInnerIp");
            return (Criteria) this;
        }

        public Criteria andCollectorExternalIpIsNull() {
            addCriterion("collector_external_ip is null");
            return (Criteria) this;
        }

        public Criteria andCollectorExternalIpIsNotNull() {
            addCriterion("collector_external_ip is not null");
            return (Criteria) this;
        }

        public Criteria andCollectorExternalIpEqualTo(String value) {
            addCriterion("collector_external_ip =", value, "collectorExternalIp");
            return (Criteria) this;
        }

        public Criteria andCollectorExternalIpNotEqualTo(String value) {
            addCriterion("collector_external_ip <>", value, "collectorExternalIp");
            return (Criteria) this;
        }

        public Criteria andCollectorExternalIpGreaterThan(String value) {
            addCriterion("collector_external_ip >", value, "collectorExternalIp");
            return (Criteria) this;
        }

        public Criteria andCollectorExternalIpGreaterThanOrEqualTo(String value) {
            addCriterion("collector_external_ip >=", value, "collectorExternalIp");
            return (Criteria) this;
        }

        public Criteria andCollectorExternalIpLessThan(String value) {
            addCriterion("collector_external_ip <", value, "collectorExternalIp");
            return (Criteria) this;
        }

        public Criteria andCollectorExternalIpLessThanOrEqualTo(String value) {
            addCriterion("collector_external_ip <=", value, "collectorExternalIp");
            return (Criteria) this;
        }

        public Criteria andCollectorExternalIpLike(String value) {
            addCriterion("collector_external_ip like", value, "collectorExternalIp");
            return (Criteria) this;
        }

        public Criteria andCollectorExternalIpNotLike(String value) {
            addCriterion("collector_external_ip not like", value, "collectorExternalIp");
            return (Criteria) this;
        }

        public Criteria andCollectorExternalIpIn(List<String> values) {
            addCriterion("collector_external_ip in", values, "collectorExternalIp");
            return (Criteria) this;
        }

        public Criteria andCollectorExternalIpNotIn(List<String> values) {
            addCriterion("collector_external_ip not in", values, "collectorExternalIp");
            return (Criteria) this;
        }

        public Criteria andCollectorExternalIpBetween(String value1, String value2) {
            addCriterion("collector_external_ip between", value1, value2, "collectorExternalIp");
            return (Criteria) this;
        }

        public Criteria andCollectorExternalIpNotBetween(String value1, String value2) {
            addCriterion("collector_external_ip not between", value1, value2, "collectorExternalIp");
            return (Criteria) this;
        }

        public Criteria andStateIsNull() {
            addCriterion("state is null");
            return (Criteria) this;
        }

        public Criteria andStateIsNotNull() {
            addCriterion("state is not null");
            return (Criteria) this;
        }

        public Criteria andStateEqualTo(String value) {
            addCriterion("state =", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotEqualTo(String value) {
            addCriterion("state <>", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThan(String value) {
            addCriterion("state >", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThanOrEqualTo(String value) {
            addCriterion("state >=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThan(String value) {
            addCriterion("state <", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThanOrEqualTo(String value) {
            addCriterion("state <=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLike(String value) {
            addCriterion("state like", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotLike(String value) {
            addCriterion("state not like", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateIn(List<String> values) {
            addCriterion("state in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotIn(List<String> values) {
            addCriterion("state not in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateBetween(String value1, String value2) {
            addCriterion("state between", value1, value2, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotBetween(String value1, String value2) {
            addCriterion("state not between", value1, value2, "state");
            return (Criteria) this;
        }

        public Criteria andHeartBeatTimeIsNull() {
            addCriterion("heart_beat_time is null");
            return (Criteria) this;
        }

        public Criteria andHeartBeatTimeIsNotNull() {
            addCriterion("heart_beat_time is not null");
            return (Criteria) this;
        }

        public Criteria andHeartBeatTimeEqualTo(Date value) {
            addCriterion("heart_beat_time =", value, "heartBeatTime");
            return (Criteria) this;
        }

        public Criteria andHeartBeatTimeNotEqualTo(Date value) {
            addCriterion("heart_beat_time <>", value, "heartBeatTime");
            return (Criteria) this;
        }

        public Criteria andHeartBeatTimeGreaterThan(Date value) {
            addCriterion("heart_beat_time >", value, "heartBeatTime");
            return (Criteria) this;
        }

        public Criteria andHeartBeatTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("heart_beat_time >=", value, "heartBeatTime");
            return (Criteria) this;
        }

        public Criteria andHeartBeatTimeLessThan(Date value) {
            addCriterion("heart_beat_time <", value, "heartBeatTime");
            return (Criteria) this;
        }

        public Criteria andHeartBeatTimeLessThanOrEqualTo(Date value) {
            addCriterion("heart_beat_time <=", value, "heartBeatTime");
            return (Criteria) this;
        }

        public Criteria andHeartBeatTimeIn(List<Date> values) {
            addCriterion("heart_beat_time in", values, "heartBeatTime");
            return (Criteria) this;
        }

        public Criteria andHeartBeatTimeNotIn(List<Date> values) {
            addCriterion("heart_beat_time not in", values, "heartBeatTime");
            return (Criteria) this;
        }

        public Criteria andHeartBeatTimeBetween(Date value1, Date value2) {
            addCriterion("heart_beat_time between", value1, value2, "heartBeatTime");
            return (Criteria) this;
        }

        public Criteria andHeartBeatTimeNotBetween(Date value1, Date value2) {
            addCriterion("heart_beat_time not between", value1, value2, "heartBeatTime");
            return (Criteria) this;
        }

        public Criteria andLostHeartbeatNumIsNull() {
            addCriterion("lost_heartbeat_num is null");
            return (Criteria) this;
        }

        public Criteria andLostHeartbeatNumIsNotNull() {
            addCriterion("lost_heartbeat_num is not null");
            return (Criteria) this;
        }

        public Criteria andLostHeartbeatNumEqualTo(Integer value) {
            addCriterion("lost_heartbeat_num =", value, "lostHeartbeatNum");
            return (Criteria) this;
        }

        public Criteria andLostHeartbeatNumNotEqualTo(Integer value) {
            addCriterion("lost_heartbeat_num <>", value, "lostHeartbeatNum");
            return (Criteria) this;
        }

        public Criteria andLostHeartbeatNumGreaterThan(Integer value) {
            addCriterion("lost_heartbeat_num >", value, "lostHeartbeatNum");
            return (Criteria) this;
        }

        public Criteria andLostHeartbeatNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("lost_heartbeat_num >=", value, "lostHeartbeatNum");
            return (Criteria) this;
        }

        public Criteria andLostHeartbeatNumLessThan(Integer value) {
            addCriterion("lost_heartbeat_num <", value, "lostHeartbeatNum");
            return (Criteria) this;
        }

        public Criteria andLostHeartbeatNumLessThanOrEqualTo(Integer value) {
            addCriterion("lost_heartbeat_num <=", value, "lostHeartbeatNum");
            return (Criteria) this;
        }

        public Criteria andLostHeartbeatNumIn(List<Integer> values) {
            addCriterion("lost_heartbeat_num in", values, "lostHeartbeatNum");
            return (Criteria) this;
        }

        public Criteria andLostHeartbeatNumNotIn(List<Integer> values) {
            addCriterion("lost_heartbeat_num not in", values, "lostHeartbeatNum");
            return (Criteria) this;
        }

        public Criteria andLostHeartbeatNumBetween(Integer value1, Integer value2) {
            addCriterion("lost_heartbeat_num between", value1, value2, "lostHeartbeatNum");
            return (Criteria) this;
        }

        public Criteria andLostHeartbeatNumNotBetween(Integer value1, Integer value2) {
            addCriterion("lost_heartbeat_num not between", value1, value2, "lostHeartbeatNum");
            return (Criteria) this;
        }

        public Criteria andProcessCapacityIsNull() {
            addCriterion("process_capacity is null");
            return (Criteria) this;
        }

        public Criteria andProcessCapacityIsNotNull() {
            addCriterion("process_capacity is not null");
            return (Criteria) this;
        }

        public Criteria andProcessCapacityEqualTo(Integer value) {
            addCriterion("process_capacity =", value, "processCapacity");
            return (Criteria) this;
        }

        public Criteria andProcessCapacityNotEqualTo(Integer value) {
            addCriterion("process_capacity <>", value, "processCapacity");
            return (Criteria) this;
        }

        public Criteria andProcessCapacityGreaterThan(Integer value) {
            addCriterion("process_capacity >", value, "processCapacity");
            return (Criteria) this;
        }

        public Criteria andProcessCapacityGreaterThanOrEqualTo(Integer value) {
            addCriterion("process_capacity >=", value, "processCapacity");
            return (Criteria) this;
        }

        public Criteria andProcessCapacityLessThan(Integer value) {
            addCriterion("process_capacity <", value, "processCapacity");
            return (Criteria) this;
        }

        public Criteria andProcessCapacityLessThanOrEqualTo(Integer value) {
            addCriterion("process_capacity <=", value, "processCapacity");
            return (Criteria) this;
        }

        public Criteria andProcessCapacityIn(List<Integer> values) {
            addCriterion("process_capacity in", values, "processCapacity");
            return (Criteria) this;
        }

        public Criteria andProcessCapacityNotIn(List<Integer> values) {
            addCriterion("process_capacity not in", values, "processCapacity");
            return (Criteria) this;
        }

        public Criteria andProcessCapacityBetween(Integer value1, Integer value2) {
            addCriterion("process_capacity between", value1, value2, "processCapacity");
            return (Criteria) this;
        }

        public Criteria andProcessCapacityNotBetween(Integer value1, Integer value2) {
            addCriterion("process_capacity not between", value1, value2, "processCapacity");
            return (Criteria) this;
        }
        public Criteria andNodeStateIsNull() {
            addCriterion("node_state is null");
            return (Criteria) this;
        }

        public Criteria andNodeStateIsNotNull() {
            addCriterion("node_state is not null");
            return (Criteria) this;
        }

        public Criteria andNodeStateEqualTo(String value) {
            addCriterion("node_state =", value, "nodeState");
            return (Criteria) this;
        }

        public Criteria andNodeStateNotEqualTo(String value) {
            addCriterion("node_state <>", value, "nodeState");
            return (Criteria) this;
        }

        public Criteria andNodeStateGreaterThan(String value) {
            addCriterion("node_state >", value, "nodeState");
            return (Criteria) this;
        }

        public Criteria andNodeStateGreaterThanOrEqualTo(String value) {
            addCriterion("node_state >=", value, "nodeState");
            return (Criteria) this;
        }

        public Criteria andNodeStateLessThan(String value) {
            addCriterion("node_state <", value, "nodeState");
            return (Criteria) this;
        }

        public Criteria andNodeStateLessThanOrEqualTo(String value) {
            addCriterion("node_state <=", value, "nodeState");
            return (Criteria) this;
        }

        public Criteria andNodeStateLike(String value) {
            addCriterion("node_state like", value, "nodeState");
            return (Criteria) this;
        }

        public Criteria andNodeStateNotLike(String value) {
            addCriterion("node_state not like", value, "nodeState");
            return (Criteria) this;
        }

        public Criteria andNodeStateIn(List<String> values) {
            addCriterion("node_state in", values, "nodeState");
            return (Criteria) this;
        }

        public Criteria andNodeStateNotIn(List<String> values) {
            addCriterion("node_state not in", values, "nodeState");
            return (Criteria) this;
        }

        public Criteria andNodeStateBetween(String value1, String value2) {
            addCriterion("node_state between", value1, value2, "nodeState");
            return (Criteria) this;
        }

        public Criteria andNodeStateNotBetween(String value1, String value2) {
            addCriterion("node_state not between", value1, value2, "nodeState");
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