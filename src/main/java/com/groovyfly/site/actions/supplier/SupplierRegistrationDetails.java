/*
 * SupplierRegistrationDetails.java
 * 
 * Copyright (c) 2013 Groovy Fly
 */
package com.groovyfly.site.actions.supplier;

import java.io.Serializable;
import java.math.BigDecimal;

import com.groovyfly.admin.structures.Supplier;

/**
 * @author Chris Hatton
 */
public class SupplierRegistrationDetails implements Serializable {

    private static final long serialVersionUID = 5931066439202105925L;

    /* step 1 - basic company details */

    private Supplier supplier;

    private YesNoPossibly englishSpeaking;

    /* step 2 - company information */

    private Integer tradingYears;

    private Integer fullTimeStaff;

    private Integer partTimeStaff;

    private Integer seasonalStaff;

    private Integer averageFliesPerMonth;

    private YesNoSome outsourcesFlies;

    /* step 3 - your fishing flies */

    private Integer averageDifferentPatterns;

    private Boolean tiesCustomPatterns;

    private Boolean usesBrandedHooks;

    private String hookBrands;

    private Boolean usesBrandedThread;

    private String threadBrands;

    private YesNoPossibly materialListSupplied;

    /* step 4 - orders placed with you */

    private Integer minOrderDozen;

    private BigDecimal minOrderDolars;

    private Integer minOrderPerPattern;

    private Integer leadTimeWeeks;

    private Integer downPayment;

    private YesNoPossibly downPaymentNegotiable;

    private BigDecimal dozenWetFliesDolars;

    /* step 5 - */

    private String[] paymentOptions;

    private String otherPaymentMethods;

    private String[] shippingMethods;

    private String otherShippingMethods;

    private Boolean freePostageLargeOrders;

    /* step 6 - other details */

    private Boolean ableToProvideReference;

    private String exampleCustomers;

    private Integer theirflyQualityRating;

    private String otherCompanyInfo;

    /**
     * Constructor
     */
    public SupplierRegistrationDetails() {
        super();
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public YesNoPossibly getEnglishSpeaking() {
        return englishSpeaking;
    }

    public void setEnglishSpeaking(YesNoPossibly englishSpeaking) {
        this.englishSpeaking = englishSpeaking;
    }

    public Integer getTradingYears() {
        return tradingYears;
    }

    public void setTradingYears(Integer tradingYears) {
        this.tradingYears = tradingYears;
    }

    public Integer getFullTimeStaff() {
        return fullTimeStaff;
    }

    public void setFullTimeStaff(Integer fullTimeStaff) {
        this.fullTimeStaff = fullTimeStaff;
    }

    public Integer getPartTimeStaff() {
        return partTimeStaff;
    }

    public void setPartTimeStaff(Integer partTimeStaff) {
        this.partTimeStaff = partTimeStaff;
    }

    public Integer getSeasonalStaff() {
        return seasonalStaff;
    }

    public void setSeasonalStaff(Integer seasonalStaff) {
        this.seasonalStaff = seasonalStaff;
    }

    public Integer getAverageFliesPerMonth() {
        return averageFliesPerMonth;
    }

    public void setAverageFliesPerMonth(Integer averageFliesPerMonth) {
        this.averageFliesPerMonth = averageFliesPerMonth;
    }

    public YesNoSome getOutsourcesFlies() {
        return outsourcesFlies;
    }

    public void setOutsourcesFlies(YesNoSome outsourcesFlies) {
        this.outsourcesFlies = outsourcesFlies;
    }

    public Integer getAverageDifferentPatterns() {
        return averageDifferentPatterns;
    }

    public void setAverageDifferentPatterns(Integer averageDifferentPatterns) {
        this.averageDifferentPatterns = averageDifferentPatterns;
    }

    public Boolean getTiesCustomPatterns() {
        return tiesCustomPatterns;
    }

    public void setTiesCustomPatterns(Boolean tiesCustomPatterns) {
        this.tiesCustomPatterns = tiesCustomPatterns;
    }

    public Boolean getUsesBrandedHooks() {
        return usesBrandedHooks;
    }

    public void setUsesBrandedHooks(Boolean usesBrandedHooks) {
        this.usesBrandedHooks = usesBrandedHooks;
    }

    public String getHookBrands() {
        return hookBrands;
    }

    public void setHookBrands(String hookBrands) {
        this.hookBrands = hookBrands;
    }

    public Boolean getUsesBrandedThread() {
        return usesBrandedThread;
    }

    public void setUsesBrandedThread(Boolean usesBrandedThread) {
        this.usesBrandedThread = usesBrandedThread;
    }

    public String getThreadBrands() {
        return threadBrands;
    }

    public void setThreadBrands(String threadBrands) {
        this.threadBrands = threadBrands;
    }

    public YesNoPossibly getMaterialListSupplied() {
        return materialListSupplied;
    }

    public void setMaterialListSupplied(YesNoPossibly materialListSupplied) {
        this.materialListSupplied = materialListSupplied;
    }

    public BigDecimal getMinOrderDolars() {
        return minOrderDolars;
    }

    public void setMinOrderDolars(BigDecimal minOrderDolars) {
        this.minOrderDolars = minOrderDolars;
    }

    public Integer getMinOrderDozen() {
        return minOrderDozen;
    }

    public void setMinOrderDozen(Integer minOrderDozen) {
        this.minOrderDozen = minOrderDozen;
    }

    public Integer getMinOrderPerPattern() {
        return minOrderPerPattern;
    }

    public void setMinOrderPerPattern(Integer minOrderPerPattern) {
        this.minOrderPerPattern = minOrderPerPattern;
    }

    public Integer getLeadTimeWeeks() {
        return leadTimeWeeks;
    }

    public void setLeadTimeWeeks(Integer leadTimeWeeks) {
        this.leadTimeWeeks = leadTimeWeeks;
    }

    public Integer getDownPayment() {
        return downPayment;
    }

    public void setDownPayment(Integer downPayment) {
        this.downPayment = downPayment;
    }

    public YesNoPossibly getDownPaymentNegotiable() {
        return downPaymentNegotiable;
    }

    public void setDownPaymentNegotiable(YesNoPossibly downPaymentNegotiable) {
        this.downPaymentNegotiable = downPaymentNegotiable;
    }

    public BigDecimal getDozenWetFliesDolars() {
        return dozenWetFliesDolars;
    }

    public void setDozenWetFliesDolars(BigDecimal dozenWetFliesDolars) {
        this.dozenWetFliesDolars = dozenWetFliesDolars;
    }

    public String[] getPaymentOptions() {
        return paymentOptions;
    }

    public void setPaymentOptions(String[] paymentOptions) {
        this.paymentOptions = paymentOptions;
    }

    public String getOtherPaymentMethods() {
        return otherPaymentMethods;
    }

    public void setOtherPaymentMethods(String otherPaymentMethods) {
        this.otherPaymentMethods = otherPaymentMethods;
    }

    public String[] getShippingMethods() {
        return shippingMethods;
    }

    public void setShippingMethods(String[] shippingMethods) {
        this.shippingMethods = shippingMethods;
    }

    public String getOtherShippingMethods() {
        return otherShippingMethods;
    }

    public void setOtherShippingMethods(String otherShippingMethods) {
        this.otherShippingMethods = otherShippingMethods;
    }

    public Boolean getFreePostageLargeOrders() {
        return freePostageLargeOrders;
    }

    public void setFreePostageLargeOrders(Boolean freePostageLargeOrders) {
        this.freePostageLargeOrders = freePostageLargeOrders;
    }

    public Boolean getAbleToProvideReference() {
        return ableToProvideReference;
    }

    public void setAbleToProvideReference(Boolean ableToProvideReference) {
        this.ableToProvideReference = ableToProvideReference;
    }

    public String getExampleCustomers() {
        return exampleCustomers;
    }

    public void setExampleCustomers(String exampleCustomers) {
        this.exampleCustomers = exampleCustomers;
    }

    public Integer getTheirflyQualityRating() {
        return theirflyQualityRating;
    }

    public void setTheirflyQualityRating(Integer theirflyQualityRating) {
        this.theirflyQualityRating = theirflyQualityRating;
    }

    public String getOtherCompanyInfo() {
        return otherCompanyInfo;
    }

    public void setOtherCompanyInfo(String otherCompanyInfo) {
        this.otherCompanyInfo = otherCompanyInfo;
    }

}
