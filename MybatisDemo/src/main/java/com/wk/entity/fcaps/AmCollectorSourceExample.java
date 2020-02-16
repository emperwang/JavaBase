package com.wk.entity.fcaps;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class AmCollectorSourceExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AmCollectorSourceExample() {
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

        protected void addCriterionForJDBCTime(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Time(value.getTime()), property);
        }

        protected void addCriterionForJDBCTime(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Time> timeList = new ArrayList<java.sql.Time>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                timeList.add(new java.sql.Time(iter.next().getTime()));
            }
            addCriterion(condition, timeList, property);
        }

        protected void addCriterionForJDBCTime(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Time(value1.getTime()), new java.sql.Time(value2.getTime()), property);
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

        public Criteria andSourceNameIsNull() {
            addCriterion("source_name is null");
            return (Criteria) this;
        }

        public Criteria andSourceNameIsNotNull() {
            addCriterion("source_name is not null");
            return (Criteria) this;
        }

        public Criteria andSourceNameEqualTo(String value) {
            addCriterion("source_name =", value, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameNotEqualTo(String value) {
            addCriterion("source_name <>", value, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameGreaterThan(String value) {
            addCriterion("source_name >", value, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameGreaterThanOrEqualTo(String value) {
            addCriterion("source_name >=", value, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameLessThan(String value) {
            addCriterion("source_name <", value, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameLessThanOrEqualTo(String value) {
            addCriterion("source_name <=", value, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameLike(String value) {
            addCriterion("source_name like", value, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameNotLike(String value) {
            addCriterion("source_name not like", value, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameIn(List<String> values) {
            addCriterion("source_name in", values, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameNotIn(List<String> values) {
            addCriterion("source_name not in", values, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameBetween(String value1, String value2) {
            addCriterion("source_name between", value1, value2, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameNotBetween(String value1, String value2) {
            addCriterion("source_name not between", value1, value2, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceTypeIsNull() {
            addCriterion("source_type is null");
            return (Criteria) this;
        }

        public Criteria andSourceTypeIsNotNull() {
            addCriterion("source_type is not null");
            return (Criteria) this;
        }

        public Criteria andSourceTypeEqualTo(String value) {
            addCriterion("source_type =", value, "sourceType");
            return (Criteria) this;
        }

        public Criteria andSourceTypeNotEqualTo(String value) {
            addCriterion("source_type <>", value, "sourceType");
            return (Criteria) this;
        }

        public Criteria andSourceTypeGreaterThan(String value) {
            addCriterion("source_type >", value, "sourceType");
            return (Criteria) this;
        }

        public Criteria andSourceTypeGreaterThanOrEqualTo(String value) {
            addCriterion("source_type >=", value, "sourceType");
            return (Criteria) this;
        }

        public Criteria andSourceTypeLessThan(String value) {
            addCriterion("source_type <", value, "sourceType");
            return (Criteria) this;
        }

        public Criteria andSourceTypeLessThanOrEqualTo(String value) {
            addCriterion("source_type <=", value, "sourceType");
            return (Criteria) this;
        }

        public Criteria andSourceTypeLike(String value) {
            addCriterion("source_type like", value, "sourceType");
            return (Criteria) this;
        }

        public Criteria andSourceTypeNotLike(String value) {
            addCriterion("source_type not like", value, "sourceType");
            return (Criteria) this;
        }

        public Criteria andSourceTypeIn(List<String> values) {
            addCriterion("source_type in", values, "sourceType");
            return (Criteria) this;
        }

        public Criteria andSourceTypeNotIn(List<String> values) {
            addCriterion("source_type not in", values, "sourceType");
            return (Criteria) this;
        }

        public Criteria andSourceTypeBetween(String value1, String value2) {
            addCriterion("source_type between", value1, value2, "sourceType");
            return (Criteria) this;
        }

        public Criteria andSourceTypeNotBetween(String value1, String value2) {
            addCriterion("source_type not between", value1, value2, "sourceType");
            return (Criteria) this;
        }

        public Criteria andVendorIdIsNull() {
            addCriterion("vendor_id is null");
            return (Criteria) this;
        }

        public Criteria andVendorIdIsNotNull() {
            addCriterion("vendor_id is not null");
            return (Criteria) this;
        }

        public Criteria andVendorIdEqualTo(String value) {
            addCriterion("vendor_id =", value, "vendorId");
            return (Criteria) this;
        }

        public Criteria andVendorIdNotEqualTo(String value) {
            addCriterion("vendor_id <>", value, "vendorId");
            return (Criteria) this;
        }

        public Criteria andVendorIdGreaterThan(String value) {
            addCriterion("vendor_id >", value, "vendorId");
            return (Criteria) this;
        }

        public Criteria andVendorIdGreaterThanOrEqualTo(String value) {
            addCriterion("vendor_id >=", value, "vendorId");
            return (Criteria) this;
        }

        public Criteria andVendorIdLessThan(String value) {
            addCriterion("vendor_id <", value, "vendorId");
            return (Criteria) this;
        }

        public Criteria andVendorIdLessThanOrEqualTo(String value) {
            addCriterion("vendor_id <=", value, "vendorId");
            return (Criteria) this;
        }

        public Criteria andVendorIdLike(String value) {
            addCriterion("vendor_id like", value, "vendorId");
            return (Criteria) this;
        }

        public Criteria andVendorIdNotLike(String value) {
            addCriterion("vendor_id not like", value, "vendorId");
            return (Criteria) this;
        }

        public Criteria andVendorIdIn(List<String> values) {
            addCriterion("vendor_id in", values, "vendorId");
            return (Criteria) this;
        }

        public Criteria andVendorIdNotIn(List<String> values) {
            addCriterion("vendor_id not in", values, "vendorId");
            return (Criteria) this;
        }

        public Criteria andVendorIdBetween(String value1, String value2) {
            addCriterion("vendor_id between", value1, value2, "vendorId");
            return (Criteria) this;
        }

        public Criteria andVendorIdNotBetween(String value1, String value2) {
            addCriterion("vendor_id not between", value1, value2, "vendorId");
            return (Criteria) this;
        }

        public Criteria andCostClassIsNull() {
            addCriterion("cost_class is null");
            return (Criteria) this;
        }

        public Criteria andCostClassIsNotNull() {
            addCriterion("cost_class is not null");
            return (Criteria) this;
        }

        public Criteria andCostClassEqualTo(String value) {
            addCriterion("cost_class =", value, "costClass");
            return (Criteria) this;
        }

        public Criteria andCostClassNotEqualTo(String value) {
            addCriterion("cost_class <>", value, "costClass");
            return (Criteria) this;
        }

        public Criteria andCostClassGreaterThan(String value) {
            addCriterion("cost_class >", value, "costClass");
            return (Criteria) this;
        }

        public Criteria andCostClassGreaterThanOrEqualTo(String value) {
            addCriterion("cost_class >=", value, "costClass");
            return (Criteria) this;
        }

        public Criteria andCostClassLessThan(String value) {
            addCriterion("cost_class <", value, "costClass");
            return (Criteria) this;
        }

        public Criteria andCostClassLessThanOrEqualTo(String value) {
            addCriterion("cost_class <=", value, "costClass");
            return (Criteria) this;
        }

        public Criteria andCostClassLike(String value) {
            addCriterion("cost_class like", value, "costClass");
            return (Criteria) this;
        }

        public Criteria andCostClassNotLike(String value) {
            addCriterion("cost_class not like", value, "costClass");
            return (Criteria) this;
        }

        public Criteria andCostClassIn(List<String> values) {
            addCriterion("cost_class in", values, "costClass");
            return (Criteria) this;
        }

        public Criteria andCostClassNotIn(List<String> values) {
            addCriterion("cost_class not in", values, "costClass");
            return (Criteria) this;
        }

        public Criteria andCostClassBetween(String value1, String value2) {
            addCriterion("cost_class between", value1, value2, "costClass");
            return (Criteria) this;
        }

        public Criteria andCostClassNotBetween(String value1, String value2) {
            addCriterion("cost_class not between", value1, value2, "costClass");
            return (Criteria) this;
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

        public Criteria andPmPeriodIsNull() {
            addCriterion("pm_period is null");
            return (Criteria) this;
        }

        public Criteria andPmPeriodIsNotNull() {
            addCriterion("pm_period is not null");
            return (Criteria) this;
        }

        public Criteria andPmPeriodEqualTo(Integer value) {
            addCriterion("pm_period =", value, "pmPeriod");
            return (Criteria) this;
        }

        public Criteria andPmPeriodNotEqualTo(Integer value) {
            addCriterion("pm_period <>", value, "pmPeriod");
            return (Criteria) this;
        }

        public Criteria andPmPeriodGreaterThan(Integer value) {
            addCriterion("pm_period >", value, "pmPeriod");
            return (Criteria) this;
        }

        public Criteria andPmPeriodGreaterThanOrEqualTo(Integer value) {
            addCriterion("pm_period >=", value, "pmPeriod");
            return (Criteria) this;
        }

        public Criteria andPmPeriodLessThan(Integer value) {
            addCriterion("pm_period <", value, "pmPeriod");
            return (Criteria) this;
        }

        public Criteria andPmPeriodLessThanOrEqualTo(Integer value) {
            addCriterion("pm_period <=", value, "pmPeriod");
            return (Criteria) this;
        }

        public Criteria andPmPeriodIn(List<Integer> values) {
            addCriterion("pm_period in", values, "pmPeriod");
            return (Criteria) this;
        }

        public Criteria andPmPeriodNotIn(List<Integer> values) {
            addCriterion("pm_period not in", values, "pmPeriod");
            return (Criteria) this;
        }

        public Criteria andPmPeriodBetween(Integer value1, Integer value2) {
            addCriterion("pm_period between", value1, value2, "pmPeriod");
            return (Criteria) this;
        }

        public Criteria andPmPeriodNotBetween(Integer value1, Integer value2) {
            addCriterion("pm_period not between", value1, value2, "pmPeriod");
            return (Criteria) this;
        }

        public Criteria andCmFmHeartBeatIsNull() {
            addCriterion("cm_fm_heart_beat is null");
            return (Criteria) this;
        }

        public Criteria andCmFmHeartBeatIsNotNull() {
            addCriterion("cm_fm_heart_beat is not null");
            return (Criteria) this;
        }

        public Criteria andCmFmHeartBeatEqualTo(Integer value) {
            addCriterion("cm_fm_heart_beat =", value, "cmFmHeartBeat");
            return (Criteria) this;
        }

        public Criteria andCmFmHeartBeatNotEqualTo(Integer value) {
            addCriterion("cm_fm_heart_beat <>", value, "cmFmHeartBeat");
            return (Criteria) this;
        }

        public Criteria andCmFmHeartBeatGreaterThan(Integer value) {
            addCriterion("cm_fm_heart_beat >", value, "cmFmHeartBeat");
            return (Criteria) this;
        }

        public Criteria andCmFmHeartBeatGreaterThanOrEqualTo(Integer value) {
            addCriterion("cm_fm_heart_beat >=", value, "cmFmHeartBeat");
            return (Criteria) this;
        }

        public Criteria andCmFmHeartBeatLessThan(Integer value) {
            addCriterion("cm_fm_heart_beat <", value, "cmFmHeartBeat");
            return (Criteria) this;
        }

        public Criteria andCmFmHeartBeatLessThanOrEqualTo(Integer value) {
            addCriterion("cm_fm_heart_beat <=", value, "cmFmHeartBeat");
            return (Criteria) this;
        }

        public Criteria andCmFmHeartBeatIn(List<Integer> values) {
            addCriterion("cm_fm_heart_beat in", values, "cmFmHeartBeat");
            return (Criteria) this;
        }

        public Criteria andCmFmHeartBeatNotIn(List<Integer> values) {
            addCriterion("cm_fm_heart_beat not in", values, "cmFmHeartBeat");
            return (Criteria) this;
        }

        public Criteria andCmFmHeartBeatBetween(Integer value1, Integer value2) {
            addCriterion("cm_fm_heart_beat between", value1, value2, "cmFmHeartBeat");
            return (Criteria) this;
        }

        public Criteria andCmFmHeartBeatNotBetween(Integer value1, Integer value2) {
            addCriterion("cm_fm_heart_beat not between", value1, value2, "cmFmHeartBeat");
            return (Criteria) this;
        }

        public Criteria andDomainTypeIsNull() {
            addCriterion("domain_type is null");
            return (Criteria) this;
        }

        public Criteria andDomainTypeIsNotNull() {
            addCriterion("domain_type is not null");
            return (Criteria) this;
        }

        public Criteria andDomainTypeEqualTo(String value) {
            addCriterion("domain_type =", value, "domainType");
            return (Criteria) this;
        }

        public Criteria andDomainTypeNotEqualTo(String value) {
            addCriterion("domain_type <>", value, "domainType");
            return (Criteria) this;
        }

        public Criteria andDomainTypeGreaterThan(String value) {
            addCriterion("domain_type >", value, "domainType");
            return (Criteria) this;
        }

        public Criteria andDomainTypeGreaterThanOrEqualTo(String value) {
            addCriterion("domain_type >=", value, "domainType");
            return (Criteria) this;
        }

        public Criteria andDomainTypeLessThan(String value) {
            addCriterion("domain_type <", value, "domainType");
            return (Criteria) this;
        }

        public Criteria andDomainTypeLessThanOrEqualTo(String value) {
            addCriterion("domain_type <=", value, "domainType");
            return (Criteria) this;
        }

        public Criteria andDomainTypeLike(String value) {
            addCriterion("domain_type like", value, "domainType");
            return (Criteria) this;
        }

        public Criteria andDomainTypeNotLike(String value) {
            addCriterion("domain_type not like", value, "domainType");
            return (Criteria) this;
        }

        public Criteria andDomainTypeIn(List<String> values) {
            addCriterion("domain_type in", values, "domainType");
            return (Criteria) this;
        }

        public Criteria andDomainTypeNotIn(List<String> values) {
            addCriterion("domain_type not in", values, "domainType");
            return (Criteria) this;
        }

        public Criteria andDomainTypeBetween(String value1, String value2) {
            addCriterion("domain_type between", value1, value2, "domainType");
            return (Criteria) this;
        }

        public Criteria andDomainTypeNotBetween(String value1, String value2) {
            addCriterion("domain_type not between", value1, value2, "domainType");
            return (Criteria) this;
        }

        public Criteria andFtpOrSftpIsNull() {
            addCriterion("ftp_or_sftp is null");
            return (Criteria) this;
        }

        public Criteria andFtpOrSftpIsNotNull() {
            addCriterion("ftp_or_sftp is not null");
            return (Criteria) this;
        }

        public Criteria andFtpOrSftpEqualTo(String value) {
            addCriterion("ftp_or_sftp =", value, "ftpOrSftp");
            return (Criteria) this;
        }

        public Criteria andFtpOrSftpNotEqualTo(String value) {
            addCriterion("ftp_or_sftp <>", value, "ftpOrSftp");
            return (Criteria) this;
        }

        public Criteria andFtpOrSftpGreaterThan(String value) {
            addCriterion("ftp_or_sftp >", value, "ftpOrSftp");
            return (Criteria) this;
        }

        public Criteria andFtpOrSftpGreaterThanOrEqualTo(String value) {
            addCriterion("ftp_or_sftp >=", value, "ftpOrSftp");
            return (Criteria) this;
        }

        public Criteria andFtpOrSftpLessThan(String value) {
            addCriterion("ftp_or_sftp <", value, "ftpOrSftp");
            return (Criteria) this;
        }

        public Criteria andFtpOrSftpLessThanOrEqualTo(String value) {
            addCriterion("ftp_or_sftp <=", value, "ftpOrSftp");
            return (Criteria) this;
        }

        public Criteria andFtpOrSftpLike(String value) {
            addCriterion("ftp_or_sftp like", value, "ftpOrSftp");
            return (Criteria) this;
        }

        public Criteria andFtpOrSftpNotLike(String value) {
            addCriterion("ftp_or_sftp not like", value, "ftpOrSftp");
            return (Criteria) this;
        }

        public Criteria andFtpOrSftpIn(List<String> values) {
            addCriterion("ftp_or_sftp in", values, "ftpOrSftp");
            return (Criteria) this;
        }

        public Criteria andFtpOrSftpNotIn(List<String> values) {
            addCriterion("ftp_or_sftp not in", values, "ftpOrSftp");
            return (Criteria) this;
        }

        public Criteria andFtpOrSftpBetween(String value1, String value2) {
            addCriterion("ftp_or_sftp between", value1, value2, "ftpOrSftp");
            return (Criteria) this;
        }

        public Criteria andFtpOrSftpNotBetween(String value1, String value2) {
            addCriterion("ftp_or_sftp not between", value1, value2, "ftpOrSftp");
            return (Criteria) this;
        }

        public Criteria andFtpIpIsNull() {
            addCriterion("ftp_ip is null");
            return (Criteria) this;
        }

        public Criteria andFtpIpIsNotNull() {
            addCriterion("ftp_ip is not null");
            return (Criteria) this;
        }

        public Criteria andFtpIpEqualTo(String value) {
            addCriterion("ftp_ip =", value, "ftpIp");
            return (Criteria) this;
        }

        public Criteria andFtpIpNotEqualTo(String value) {
            addCriterion("ftp_ip <>", value, "ftpIp");
            return (Criteria) this;
        }

        public Criteria andFtpIpGreaterThan(String value) {
            addCriterion("ftp_ip >", value, "ftpIp");
            return (Criteria) this;
        }

        public Criteria andFtpIpGreaterThanOrEqualTo(String value) {
            addCriterion("ftp_ip >=", value, "ftpIp");
            return (Criteria) this;
        }

        public Criteria andFtpIpLessThan(String value) {
            addCriterion("ftp_ip <", value, "ftpIp");
            return (Criteria) this;
        }

        public Criteria andFtpIpLessThanOrEqualTo(String value) {
            addCriterion("ftp_ip <=", value, "ftpIp");
            return (Criteria) this;
        }

        public Criteria andFtpIpLike(String value) {
            addCriterion("ftp_ip like", value, "ftpIp");
            return (Criteria) this;
        }

        public Criteria andFtpIpNotLike(String value) {
            addCriterion("ftp_ip not like", value, "ftpIp");
            return (Criteria) this;
        }

        public Criteria andFtpIpIn(List<String> values) {
            addCriterion("ftp_ip in", values, "ftpIp");
            return (Criteria) this;
        }

        public Criteria andFtpIpNotIn(List<String> values) {
            addCriterion("ftp_ip not in", values, "ftpIp");
            return (Criteria) this;
        }

        public Criteria andFtpIpBetween(String value1, String value2) {
            addCriterion("ftp_ip between", value1, value2, "ftpIp");
            return (Criteria) this;
        }

        public Criteria andFtpIpNotBetween(String value1, String value2) {
            addCriterion("ftp_ip not between", value1, value2, "ftpIp");
            return (Criteria) this;
        }

        public Criteria andFtpPortIsNull() {
            addCriterion("ftp_port is null");
            return (Criteria) this;
        }

        public Criteria andFtpPortIsNotNull() {
            addCriterion("ftp_port is not null");
            return (Criteria) this;
        }

        public Criteria andFtpPortEqualTo(Integer value) {
            addCriterion("ftp_port =", value, "ftpPort");
            return (Criteria) this;
        }

        public Criteria andFtpPortNotEqualTo(Integer value) {
            addCriterion("ftp_port <>", value, "ftpPort");
            return (Criteria) this;
        }

        public Criteria andFtpPortGreaterThan(Integer value) {
            addCriterion("ftp_port >", value, "ftpPort");
            return (Criteria) this;
        }

        public Criteria andFtpPortGreaterThanOrEqualTo(Integer value) {
            addCriterion("ftp_port >=", value, "ftpPort");
            return (Criteria) this;
        }

        public Criteria andFtpPortLessThan(Integer value) {
            addCriterion("ftp_port <", value, "ftpPort");
            return (Criteria) this;
        }

        public Criteria andFtpPortLessThanOrEqualTo(Integer value) {
            addCriterion("ftp_port <=", value, "ftpPort");
            return (Criteria) this;
        }

        public Criteria andFtpPortIn(List<Integer> values) {
            addCriterion("ftp_port in", values, "ftpPort");
            return (Criteria) this;
        }

        public Criteria andFtpPortNotIn(List<Integer> values) {
            addCriterion("ftp_port not in", values, "ftpPort");
            return (Criteria) this;
        }

        public Criteria andFtpPortBetween(Integer value1, Integer value2) {
            addCriterion("ftp_port between", value1, value2, "ftpPort");
            return (Criteria) this;
        }

        public Criteria andFtpPortNotBetween(Integer value1, Integer value2) {
            addCriterion("ftp_port not between", value1, value2, "ftpPort");
            return (Criteria) this;
        }

        public Criteria andFtpUserIsNull() {
            addCriterion("ftp_user is null");
            return (Criteria) this;
        }

        public Criteria andFtpUserIsNotNull() {
            addCriterion("ftp_user is not null");
            return (Criteria) this;
        }

        public Criteria andFtpUserEqualTo(String value) {
            addCriterion("ftp_user =", value, "ftpUser");
            return (Criteria) this;
        }

        public Criteria andFtpUserNotEqualTo(String value) {
            addCriterion("ftp_user <>", value, "ftpUser");
            return (Criteria) this;
        }

        public Criteria andFtpUserGreaterThan(String value) {
            addCriterion("ftp_user >", value, "ftpUser");
            return (Criteria) this;
        }

        public Criteria andFtpUserGreaterThanOrEqualTo(String value) {
            addCriterion("ftp_user >=", value, "ftpUser");
            return (Criteria) this;
        }

        public Criteria andFtpUserLessThan(String value) {
            addCriterion("ftp_user <", value, "ftpUser");
            return (Criteria) this;
        }

        public Criteria andFtpUserLessThanOrEqualTo(String value) {
            addCriterion("ftp_user <=", value, "ftpUser");
            return (Criteria) this;
        }

        public Criteria andFtpUserLike(String value) {
            addCriterion("ftp_user like", value, "ftpUser");
            return (Criteria) this;
        }

        public Criteria andFtpUserNotLike(String value) {
            addCriterion("ftp_user not like", value, "ftpUser");
            return (Criteria) this;
        }

        public Criteria andFtpUserIn(List<String> values) {
            addCriterion("ftp_user in", values, "ftpUser");
            return (Criteria) this;
        }

        public Criteria andFtpUserNotIn(List<String> values) {
            addCriterion("ftp_user not in", values, "ftpUser");
            return (Criteria) this;
        }

        public Criteria andFtpUserBetween(String value1, String value2) {
            addCriterion("ftp_user between", value1, value2, "ftpUser");
            return (Criteria) this;
        }

        public Criteria andFtpUserNotBetween(String value1, String value2) {
            addCriterion("ftp_user not between", value1, value2, "ftpUser");
            return (Criteria) this;
        }

        public Criteria andFtpPasswordIsNull() {
            addCriterion("ftp_password is null");
            return (Criteria) this;
        }

        public Criteria andFtpPasswordIsNotNull() {
            addCriterion("ftp_password is not null");
            return (Criteria) this;
        }

        public Criteria andFtpPasswordEqualTo(String value) {
            addCriterion("ftp_password =", value, "ftpPassword");
            return (Criteria) this;
        }

        public Criteria andFtpPasswordNotEqualTo(String value) {
            addCriterion("ftp_password <>", value, "ftpPassword");
            return (Criteria) this;
        }

        public Criteria andFtpPasswordGreaterThan(String value) {
            addCriterion("ftp_password >", value, "ftpPassword");
            return (Criteria) this;
        }

        public Criteria andFtpPasswordGreaterThanOrEqualTo(String value) {
            addCriterion("ftp_password >=", value, "ftpPassword");
            return (Criteria) this;
        }

        public Criteria andFtpPasswordLessThan(String value) {
            addCriterion("ftp_password <", value, "ftpPassword");
            return (Criteria) this;
        }

        public Criteria andFtpPasswordLessThanOrEqualTo(String value) {
            addCriterion("ftp_password <=", value, "ftpPassword");
            return (Criteria) this;
        }

        public Criteria andFtpPasswordLike(String value) {
            addCriterion("ftp_password like", value, "ftpPassword");
            return (Criteria) this;
        }

        public Criteria andFtpPasswordNotLike(String value) {
            addCriterion("ftp_password not like", value, "ftpPassword");
            return (Criteria) this;
        }

        public Criteria andFtpPasswordIn(List<String> values) {
            addCriterion("ftp_password in", values, "ftpPassword");
            return (Criteria) this;
        }

        public Criteria andFtpPasswordNotIn(List<String> values) {
            addCriterion("ftp_password not in", values, "ftpPassword");
            return (Criteria) this;
        }

        public Criteria andFtpPasswordBetween(String value1, String value2) {
            addCriterion("ftp_password between", value1, value2, "ftpPassword");
            return (Criteria) this;
        }

        public Criteria andFtpPasswordNotBetween(String value1, String value2) {
            addCriterion("ftp_password not between", value1, value2, "ftpPassword");
            return (Criteria) this;
        }

        public Criteria andSocketIpIsNull() {
            addCriterion("socket_ip is null");
            return (Criteria) this;
        }

        public Criteria andSocketIpIsNotNull() {
            addCriterion("socket_ip is not null");
            return (Criteria) this;
        }

        public Criteria andSocketIpEqualTo(String value) {
            addCriterion("socket_ip =", value, "socketIp");
            return (Criteria) this;
        }

        public Criteria andSocketIpNotEqualTo(String value) {
            addCriterion("socket_ip <>", value, "socketIp");
            return (Criteria) this;
        }

        public Criteria andSocketIpGreaterThan(String value) {
            addCriterion("socket_ip >", value, "socketIp");
            return (Criteria) this;
        }

        public Criteria andSocketIpGreaterThanOrEqualTo(String value) {
            addCriterion("socket_ip >=", value, "socketIp");
            return (Criteria) this;
        }

        public Criteria andSocketIpLessThan(String value) {
            addCriterion("socket_ip <", value, "socketIp");
            return (Criteria) this;
        }

        public Criteria andSocketIpLessThanOrEqualTo(String value) {
            addCriterion("socket_ip <=", value, "socketIp");
            return (Criteria) this;
        }

        public Criteria andSocketIpLike(String value) {
            addCriterion("socket_ip like", value, "socketIp");
            return (Criteria) this;
        }

        public Criteria andSocketIpNotLike(String value) {
            addCriterion("socket_ip not like", value, "socketIp");
            return (Criteria) this;
        }

        public Criteria andSocketIpIn(List<String> values) {
            addCriterion("socket_ip in", values, "socketIp");
            return (Criteria) this;
        }

        public Criteria andSocketIpNotIn(List<String> values) {
            addCriterion("socket_ip not in", values, "socketIp");
            return (Criteria) this;
        }

        public Criteria andSocketIpBetween(String value1, String value2) {
            addCriterion("socket_ip between", value1, value2, "socketIp");
            return (Criteria) this;
        }

        public Criteria andSocketIpNotBetween(String value1, String value2) {
            addCriterion("socket_ip not between", value1, value2, "socketIp");
            return (Criteria) this;
        }

        public Criteria andSocketPortIsNull() {
            addCriterion("socket_port is null");
            return (Criteria) this;
        }

        public Criteria andSocketPortIsNotNull() {
            addCriterion("socket_port is not null");
            return (Criteria) this;
        }

        public Criteria andSocketPortEqualTo(Integer value) {
            addCriterion("socket_port =", value, "socketPort");
            return (Criteria) this;
        }

        public Criteria andSocketPortNotEqualTo(Integer value) {
            addCriterion("socket_port <>", value, "socketPort");
            return (Criteria) this;
        }

        public Criteria andSocketPortGreaterThan(Integer value) {
            addCriterion("socket_port >", value, "socketPort");
            return (Criteria) this;
        }

        public Criteria andSocketPortGreaterThanOrEqualTo(Integer value) {
            addCriterion("socket_port >=", value, "socketPort");
            return (Criteria) this;
        }

        public Criteria andSocketPortLessThan(Integer value) {
            addCriterion("socket_port <", value, "socketPort");
            return (Criteria) this;
        }

        public Criteria andSocketPortLessThanOrEqualTo(Integer value) {
            addCriterion("socket_port <=", value, "socketPort");
            return (Criteria) this;
        }

        public Criteria andSocketPortIn(List<Integer> values) {
            addCriterion("socket_port in", values, "socketPort");
            return (Criteria) this;
        }

        public Criteria andSocketPortNotIn(List<Integer> values) {
            addCriterion("socket_port not in", values, "socketPort");
            return (Criteria) this;
        }

        public Criteria andSocketPortBetween(Integer value1, Integer value2) {
            addCriterion("socket_port between", value1, value2, "socketPort");
            return (Criteria) this;
        }

        public Criteria andSocketPortNotBetween(Integer value1, Integer value2) {
            addCriterion("socket_port not between", value1, value2, "socketPort");
            return (Criteria) this;
        }

        public Criteria andSocketUserIsNull() {
            addCriterion("socket_user is null");
            return (Criteria) this;
        }

        public Criteria andSocketUserIsNotNull() {
            addCriterion("socket_user is not null");
            return (Criteria) this;
        }

        public Criteria andSocketUserEqualTo(String value) {
            addCriterion("socket_user =", value, "socketUser");
            return (Criteria) this;
        }

        public Criteria andSocketUserNotEqualTo(String value) {
            addCriterion("socket_user <>", value, "socketUser");
            return (Criteria) this;
        }

        public Criteria andSocketUserGreaterThan(String value) {
            addCriterion("socket_user >", value, "socketUser");
            return (Criteria) this;
        }

        public Criteria andSocketUserGreaterThanOrEqualTo(String value) {
            addCriterion("socket_user >=", value, "socketUser");
            return (Criteria) this;
        }

        public Criteria andSocketUserLessThan(String value) {
            addCriterion("socket_user <", value, "socketUser");
            return (Criteria) this;
        }

        public Criteria andSocketUserLessThanOrEqualTo(String value) {
            addCriterion("socket_user <=", value, "socketUser");
            return (Criteria) this;
        }

        public Criteria andSocketUserLike(String value) {
            addCriterion("socket_user like", value, "socketUser");
            return (Criteria) this;
        }

        public Criteria andSocketUserNotLike(String value) {
            addCriterion("socket_user not like", value, "socketUser");
            return (Criteria) this;
        }

        public Criteria andSocketUserIn(List<String> values) {
            addCriterion("socket_user in", values, "socketUser");
            return (Criteria) this;
        }

        public Criteria andSocketUserNotIn(List<String> values) {
            addCriterion("socket_user not in", values, "socketUser");
            return (Criteria) this;
        }

        public Criteria andSocketUserBetween(String value1, String value2) {
            addCriterion("socket_user between", value1, value2, "socketUser");
            return (Criteria) this;
        }

        public Criteria andSocketUserNotBetween(String value1, String value2) {
            addCriterion("socket_user not between", value1, value2, "socketUser");
            return (Criteria) this;
        }

        public Criteria andSocketPasswordIsNull() {
            addCriterion("socket_password is null");
            return (Criteria) this;
        }

        public Criteria andSocketPasswordIsNotNull() {
            addCriterion("socket_password is not null");
            return (Criteria) this;
        }

        public Criteria andSocketPasswordEqualTo(String value) {
            addCriterion("socket_password =", value, "socketPassword");
            return (Criteria) this;
        }

        public Criteria andSocketPasswordNotEqualTo(String value) {
            addCriterion("socket_password <>", value, "socketPassword");
            return (Criteria) this;
        }

        public Criteria andSocketPasswordGreaterThan(String value) {
            addCriterion("socket_password >", value, "socketPassword");
            return (Criteria) this;
        }

        public Criteria andSocketPasswordGreaterThanOrEqualTo(String value) {
            addCriterion("socket_password >=", value, "socketPassword");
            return (Criteria) this;
        }

        public Criteria andSocketPasswordLessThan(String value) {
            addCriterion("socket_password <", value, "socketPassword");
            return (Criteria) this;
        }

        public Criteria andSocketPasswordLessThanOrEqualTo(String value) {
            addCriterion("socket_password <=", value, "socketPassword");
            return (Criteria) this;
        }

        public Criteria andSocketPasswordLike(String value) {
            addCriterion("socket_password like", value, "socketPassword");
            return (Criteria) this;
        }

        public Criteria andSocketPasswordNotLike(String value) {
            addCriterion("socket_password not like", value, "socketPassword");
            return (Criteria) this;
        }

        public Criteria andSocketPasswordIn(List<String> values) {
            addCriterion("socket_password in", values, "socketPassword");
            return (Criteria) this;
        }

        public Criteria andSocketPasswordNotIn(List<String> values) {
            addCriterion("socket_password not in", values, "socketPassword");
            return (Criteria) this;
        }

        public Criteria andSocketPasswordBetween(String value1, String value2) {
            addCriterion("socket_password between", value1, value2, "socketPassword");
            return (Criteria) this;
        }

        public Criteria andSocketPasswordNotBetween(String value1, String value2) {
            addCriterion("socket_password not between", value1, value2, "socketPassword");
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

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterionForJDBCTime("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterionForJDBCTime("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterionForJDBCTime("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCTime("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterionForJDBCTime("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterionForJDBCTime("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterionForJDBCTime("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterionForJDBCTime("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterionForJDBCTime("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterionForJDBCTime("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }
        public Criteria andVnfmEndpointUrlIsNull() {
            addCriterion("vnfm_endpoint_url is null");
            return (Criteria) this;
        }

        public Criteria andVnfmEndpointUrlIsNotNull() {
            addCriterion("vnfm_endpoint_url is not null");
            return (Criteria) this;
        }

        public Criteria andVnfmEndpointUrlEqualTo(String value) {
            addCriterion("vnfm_endpoint_url =", value, "vnfmEndpointUrl");
            return (Criteria) this;
        }

        public Criteria andVnfmEndpointUrlNotEqualTo(String value) {
            addCriterion("vnfm_endpoint_url <>", value, "vnfmEndpointUrl");
            return (Criteria) this;
        }

        public Criteria andVnfmEndpointUrlGreaterThan(String value) {
            addCriterion("vnfm_endpoint_url >", value, "vnfmEndpointUrl");
            return (Criteria) this;
        }

        public Criteria andVnfmEndpointUrlGreaterThanOrEqualTo(String value) {
            addCriterion("vnfm_endpoint_url >=", value, "vnfmEndpointUrl");
            return (Criteria) this;
        }

        public Criteria andVnfmEndpointUrlLessThan(String value) {
            addCriterion("vnfm_endpoint_url <", value, "vnfmEndpointUrl");
            return (Criteria) this;
        }

        public Criteria andVnfmEndpointUrlLessThanOrEqualTo(String value) {
            addCriterion("vnfm_endpoint_url <=", value, "vnfmEndpointUrl");
            return (Criteria) this;
        }

        public Criteria andVnfmEndpointUrlLike(String value) {
            addCriterion("vnfm_endpoint_url like", value, "vnfmEndpointUrl");
            return (Criteria) this;
        }

        public Criteria andVnfmEndpointUrlNotLike(String value) {
            addCriterion("vnfm_endpoint_url not like", value, "vnfmEndpointUrl");
            return (Criteria) this;
        }

        public Criteria andVnfmEndpointUrlIn(List<String> values) {
            addCriterion("vnfm_endpoint_url in", values, "vnfmEndpointUrl");
            return (Criteria) this;
        }

        public Criteria andVnfmEndpointUrlNotIn(List<String> values) {
            addCriterion("vnfm_endpoint_url not in", values, "vnfmEndpointUrl");
            return (Criteria) this;
        }

        public Criteria andVnfmEndpointUrlBetween(String value1, String value2) {
            addCriterion("vnfm_endpoint_url between", value1, value2, "vnfmEndpointUrl");
            return (Criteria) this;
        }

        public Criteria andVnfmEndpointUrlNotBetween(String value1, String value2) {
            addCriterion("vnfm_endpoint_url not between", value1, value2, "vnfmEndpointUrl");
            return (Criteria) this;
        }

        public Criteria andVnfmUsernameIsNull() {
            addCriterion("vnfm_username is null");
            return (Criteria) this;
        }

        public Criteria andVnfmUsernameIsNotNull() {
            addCriterion("vnfm_username is not null");
            return (Criteria) this;
        }

        public Criteria andVnfmUsernameEqualTo(String value) {
            addCriterion("vnfm_username =", value, "vnfmUsername");
            return (Criteria) this;
        }

        public Criteria andVnfmUsernameNotEqualTo(String value) {
            addCriterion("vnfm_username <>", value, "vnfmUsername");
            return (Criteria) this;
        }

        public Criteria andVnfmUsernameGreaterThan(String value) {
            addCriterion("vnfm_username >", value, "vnfmUsername");
            return (Criteria) this;
        }

        public Criteria andVnfmUsernameGreaterThanOrEqualTo(String value) {
            addCriterion("vnfm_username >=", value, "vnfmUsername");
            return (Criteria) this;
        }

        public Criteria andVnfmUsernameLessThan(String value) {
            addCriterion("vnfm_username <", value, "vnfmUsername");
            return (Criteria) this;
        }

        public Criteria andVnfmUsernameLessThanOrEqualTo(String value) {
            addCriterion("vnfm_username <=", value, "vnfmUsername");
            return (Criteria) this;
        }

        public Criteria andVnfmUsernameLike(String value) {
            addCriterion("vnfm_username like", value, "vnfmUsername");
            return (Criteria) this;
        }

        public Criteria andVnfmUsernameNotLike(String value) {
            addCriterion("vnfm_username not like", value, "vnfmUsername");
            return (Criteria) this;
        }

        public Criteria andVnfmUsernameIn(List<String> values) {
            addCriterion("vnfm_username in", values, "vnfmUsername");
            return (Criteria) this;
        }

        public Criteria andVnfmUsernameNotIn(List<String> values) {
            addCriterion("vnfm_username not in", values, "vnfmUsername");
            return (Criteria) this;
        }

        public Criteria andVnfmUsernameBetween(String value1, String value2) {
            addCriterion("vnfm_username between", value1, value2, "vnfmUsername");
            return (Criteria) this;
        }

        public Criteria andVnfmUsernameNotBetween(String value1, String value2) {
            addCriterion("vnfm_username not between", value1, value2, "vnfmUsername");
            return (Criteria) this;
        }

        public Criteria andVnfmPasswordIsNull() {
            addCriterion("vnfm_password is null");
            return (Criteria) this;
        }

        public Criteria andVnfmPasswordIsNotNull() {
            addCriterion("vnfm_password is not null");
            return (Criteria) this;
        }

        public Criteria andVnfmPasswordEqualTo(String value) {
            addCriterion("vnfm_password =", value, "vnfmPassword");
            return (Criteria) this;
        }

        public Criteria andVnfmPasswordNotEqualTo(String value) {
            addCriterion("vnfm_password <>", value, "vnfmPassword");
            return (Criteria) this;
        }

        public Criteria andVnfmPasswordGreaterThan(String value) {
            addCriterion("vnfm_password >", value, "vnfmPassword");
            return (Criteria) this;
        }

        public Criteria andVnfmPasswordGreaterThanOrEqualTo(String value) {
            addCriterion("vnfm_password >=", value, "vnfmPassword");
            return (Criteria) this;
        }

        public Criteria andVnfmPasswordLessThan(String value) {
            addCriterion("vnfm_password <", value, "vnfmPassword");
            return (Criteria) this;
        }

        public Criteria andVnfmPasswordLessThanOrEqualTo(String value) {
            addCriterion("vnfm_password <=", value, "vnfmPassword");
            return (Criteria) this;
        }

        public Criteria andVnfmPasswordLike(String value) {
            addCriterion("vnfm_password like", value, "vnfmPassword");
            return (Criteria) this;
        }

        public Criteria andVnfmPasswordNotLike(String value) {
            addCriterion("vnfm_password not like", value, "vnfmPassword");
            return (Criteria) this;
        }

        public Criteria andVnfmPasswordIn(List<String> values) {
            addCriterion("vnfm_password in", values, "vnfmPassword");
            return (Criteria) this;
        }

        public Criteria andVnfmPasswordNotIn(List<String> values) {
            addCriterion("vnfm_password not in", values, "vnfmPassword");
            return (Criteria) this;
        }

        public Criteria andVnfmPasswordBetween(String value1, String value2) {
            addCriterion("vnfm_password between", value1, value2, "vnfmPassword");
            return (Criteria) this;
        }

        public Criteria andVnfmPasswordNotBetween(String value1, String value2) {
            addCriterion("vnfm_password not between", value1, value2, "vnfmPassword");
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