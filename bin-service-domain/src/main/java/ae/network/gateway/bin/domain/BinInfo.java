/*
 * Copyright (c) 2018 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.domain;

import ae.network.digital.platform.domain.entities.Country;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Strings;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BinInfo {

    private static final List<String> COUNTRY_CODES_IN_GCC = Arrays.asList("AE", "BH", "KW", "OM", "QA", "SA");

    @JsonProperty
    private final String bin;

    @JsonProperty
    private String cardBrand;

    @JsonProperty
    private String issuingOrg;

    @JsonProperty
    private CardType cardType;

    @JsonProperty
    private String cardCategory;

    @JsonProperty
    private String issuingCountryIsoName;

    @JsonProperty
    private String issuingCountry;

    @JsonProperty
    private String issuingCountryIsoA3Code;

    @JsonProperty
    private String issuingCountryCode;

    @JsonProperty
    private String issuingOrganizationWebsite;

    @JsonProperty
    private String issuingOrganizationPhoneNumber;

    @JsonProperty
    private boolean personalCard;

    @JsonProperty
    private boolean countryInGcc;

    @JsonProperty
    private String cardDesignation;

    @JsonProperty
    private String coBadgedInfo;

    private BinInfo(String bin) {
        this.bin = Objects.requireNonNull(bin);
    }

    public String getBin() {
        return bin;
    }

    public String getCardBrand() {
        return cardBrand;
    }

    public String getIssuingOrg() {
        return issuingOrg;
    }

    public CardType getCardType() {
        return cardType;
    }

    public String getCardCategory() {
        return cardCategory;
    }

    public String getIssuingCountryIsoName() {
        return issuingCountryIsoName;
    }

    public String getIssuingCountry() {
        return issuingCountry;
    }

    public String getIssuingCountryIsoA3Code() {
        return issuingCountryIsoA3Code;
    }

    public String getIssuingCountryCode() {
        return issuingCountryCode;
    }

    public String getIssuingOrganizationWebsite() {
        return issuingOrganizationWebsite;
    }

    public String getIssuingOrganizationPhoneNumber() {
        return issuingOrganizationPhoneNumber;
    }

    public boolean isPersonalCard() {
        return personalCard;
    }

    public String getCardDesignation() {
        return cardDesignation;
    }

    public boolean isCountryInGcc() {
        return countryInGcc;
    }

    public String getCoBadgedInfo() {
        return coBadgedInfo;
    }

    public void setAccountRangeInfo(AccountRangeInfo accountRangeInfo) {
        if (accountRangeInfo.isTokenAccountRange()) {
            final Country country = Country.getByCode(accountRangeInfo.getIssuingCountryCode());
            this.issuingCountryIsoName = country.getName();
            this.issuingCountryCode = country.getNumeric();
            this.issuingCountry = country.getAlpha2();
            this.issuingCountryIsoA3Code = country.getAlpha3();
        }
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null || getClass() != that.getClass()) {
            return false;
        }

        BinInfo binInfo = (BinInfo) that;
        return personalCard == binInfo.personalCard
                && countryInGcc == binInfo.countryInGcc
                && Objects.equals(bin, binInfo.bin)
                && Objects.equals(cardBrand, binInfo.cardBrand)
                && Objects.equals(issuingOrg, binInfo.issuingOrg)
                && cardType == binInfo.cardType
                && Objects.equals(cardCategory, binInfo.cardCategory)
                && Objects.equals(issuingCountryIsoName, binInfo.issuingCountryIsoName)
                && Objects.equals(issuingCountry, binInfo.issuingCountry)
                && Objects.equals(issuingCountryIsoA3Code, binInfo.issuingCountryIsoA3Code)
                && Objects.equals(issuingCountryCode, binInfo.issuingCountryCode)
                && Objects.equals(issuingOrganizationWebsite, binInfo.issuingOrganizationWebsite)
                && Objects.equals(issuingOrganizationPhoneNumber, binInfo.issuingOrganizationPhoneNumber)
                && Objects.equals(cardDesignation, binInfo.cardDesignation)
                && Objects.equals(coBadgedInfo, binInfo.coBadgedInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bin, cardBrand, issuingOrg, cardType, cardCategory, issuingCountryIsoName, issuingCountry,
                issuingCountryIsoA3Code, issuingCountryCode, issuingOrganizationWebsite, issuingOrganizationPhoneNumber,
                personalCard, countryInGcc, cardDesignation, coBadgedInfo);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String bin;
        private String cardBrand;
        private String cardCategory;
        private String issuingCountryIsoName;
        private String issuingOrg;
        private CardType cardType;
        private boolean personalCard;
        private String issuingCountry;
        private boolean countryInGcc;
        private String issuingCountryIsoA3Code;
        private String issuingCountryCode;
        private String issuingOrganizationWebsite;
        private String issuingOrganizationPhoneNumber;
        private String cardDesignation;

        private String coBadgedInfo;

        public String getBin() {
            return bin;
        }

        public String getCardBrand() {
            return cardBrand;
        }

        public String getCardCategory() {
            return cardCategory;
        }

        public String getIssuingCountryIsoName() {
            return issuingCountryIsoName;
        }

        public String getIssuingOrg() {
            return issuingOrg;
        }

        public CardType getCardType() {
            return cardType;
        }

        public boolean isPersonalCard() {
            return personalCard;
        }

        public String getIssuingCountry() {
            return issuingCountry;
        }

        public boolean isCountryInGcc() {
            return countryInGcc;
        }

        public String getIssuingCountryIsoA3Code() {
            return issuingCountryIsoA3Code;
        }

        public String getIssuingCountryCode() {
            return issuingCountryCode;
        }

        public String getIssuingOrganizationWebsite() {
            return issuingOrganizationWebsite;
        }

        public String getIssuingOrganizationPhoneNumber() {
            return issuingOrganizationPhoneNumber;
        }

        public String getCardDesignation() {
            return cardDesignation;
        }

        public String getCoBadgedInfo() {
            return coBadgedInfo;
        }

        public Builder withBin(String bin) {
            this.bin = bin;
            return this;
        }

        public Builder withCardBrand(String cardBrand) {
            this.cardBrand = cardBrand;
            return this;
        }

        public Builder withCardCategory(String cardCategory) {
            this.cardCategory = cardCategory;
            return this;
        }

        public Builder withIssuingCountryIsoName(String issuingCountryIsoName) {
            this.issuingCountryIsoName = issuingCountryIsoName;
            return this;
        }

        public Builder withIssuingCountry(String issuingCountry) {
            this.issuingCountry = issuingCountry;
            countryInGcc = COUNTRY_CODES_IN_GCC.contains(issuingCountry);
            return this;
        }

        public Builder withIssuingCountryIsoA3Code(String issuingCountryIsoA3Code) {
            this.issuingCountryIsoA3Code = issuingCountryIsoA3Code;
            return this;
        }

        public Builder withIssuingCountryCode(String issuingCountryCode) {
            this.issuingCountryCode = issuingCountryCode;
            return this;
        }


        public Builder withIssuingOrg(String issuingOrg) {
            this.issuingOrg = issuingOrg;
            return this;
        }

        public Builder withIssuingOrgnizationWebsite(String issuingOrganizationWebsite) {
            this.issuingOrganizationWebsite = issuingOrganizationWebsite;
            return this;
        }

        public Builder withIssuingOrgnizationPhoneNumber(String issuingOrganizationPhoneNumber) {
            this.issuingOrganizationPhoneNumber = issuingOrganizationPhoneNumber;
            return this;
        }

        public Builder withCardType(String cardType) {
            if (Strings.isNullOrEmpty(cardType)) {
                this.cardType = CardType.UNKNOWN;
            } else {
                this.cardType = CardType.valueOf(cardType.replace(' ', '_'));
            }
            return this;
        }

        public Builder withCardDesignation(String cardDesignation) {
            if (cardDesignation != null && !cardDesignation.isEmpty()) {
                this.cardDesignation = cardDesignation;
            }

            if ("PERSONAL".equals(cardDesignation)) {
                this.personalCard = true;
            }
            return this;
        }

        public Builder withCoBadgedInfo(String coBadgedInfo) {
            this.coBadgedInfo = coBadgedInfo;
            return this;
        }

        public BinInfo build() {
            BinInfo binInfo = new BinInfo(this.bin);
            binInfo.cardBrand = this.cardBrand;
            binInfo.cardCategory = this.cardCategory;
            binInfo.issuingCountryIsoName = this.issuingCountryIsoName;
            binInfo.issuingCountry = this.issuingCountry;
            binInfo.issuingCountryIsoA3Code = this.issuingCountryIsoA3Code;
            binInfo.issuingCountryCode = this.issuingCountryCode;
            binInfo.issuingOrg = this.issuingOrg;
            binInfo.issuingOrganizationWebsite = this.issuingOrganizationWebsite;
            binInfo.issuingOrganizationPhoneNumber = this.issuingOrganizationPhoneNumber;
            binInfo.cardType = this.cardType;
            binInfo.personalCard = this.personalCard;
            binInfo.countryInGcc = this.countryInGcc;
            binInfo.cardDesignation = this.cardDesignation;
            binInfo.coBadgedInfo = this.coBadgedInfo;
            return binInfo;
        }
    }
}
