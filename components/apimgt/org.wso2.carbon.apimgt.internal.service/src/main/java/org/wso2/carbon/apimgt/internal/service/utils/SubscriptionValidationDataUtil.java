/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.apimgt.internal.service.utils;

import edu.emory.mathcs.backport.java.util.Arrays;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.ext.MessageContext;
import org.wso2.carbon.apimgt.api.UnsupportedThrottleLimitTypeException;
import org.wso2.carbon.apimgt.api.dto.ConditionDTO;
import org.wso2.carbon.apimgt.api.model.Scope;
import org.wso2.carbon.apimgt.api.model.policy.BandwidthLimit;
import org.wso2.carbon.apimgt.api.model.policy.PolicyConstants;
import org.wso2.carbon.apimgt.api.model.policy.QuotaPolicy;
import org.wso2.carbon.apimgt.api.model.policy.RequestCountLimit;
import org.wso2.carbon.apimgt.api.model.subscription.*;
import org.wso2.carbon.apimgt.internal.service.dto.*;
import org.wso2.carbon.apimgt.rest.api.util.utils.RestApiUtil;
import org.wso2.carbon.utils.multitenancy.MultitenantConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SubscriptionValidationDataUtil {

    private static APIDTO fromAPItoDTO(API model) {

        APIDTO apidto = null;
        if (model != null) {
            apidto = new APIDTO();
            apidto.setApiId(model.getApiId());
            apidto.setVersion(model.getVersion());
            apidto.setName(model.getName());
            apidto.setContext(model.getContext());
            apidto.setPolicy(model.getPolicy());
            apidto.setProvider(model.getProvider());
            apidto.setApiType(model.getApiType());
            apidto.setName(model.getName());
            apidto.setIsDefaultVersion(model.isDefaultVersion());
            Map<String,URLMapping> urlMappings = model.getAllResources();
            List<URLMappingDTO> urlMappingsDTO = new ArrayList<>();
            for (URLMapping urlMapping : urlMappings.values()) {
                URLMappingDTO urlMappingDTO = new URLMappingDTO();
                urlMappingDTO.setAuthScheme(urlMapping.getAuthScheme());
                urlMappingDTO.setHttpMethod(urlMapping.getHttpMethod());
                urlMappingDTO.setThrottlingPolicy(urlMapping.getThrottlingPolicy());
                urlMappingDTO.setUrlPattern(urlMapping.getUrlPattern());
                urlMappingDTO.setScopes(urlMapping.getScopes());
                urlMappingsDTO.add(urlMappingDTO);
            }
            apidto.setUrlMappings(urlMappingsDTO);
        }
        return apidto;
    }

    public static APIListDTO fromAPIToAPIListDTO(API model) {

        APIListDTO apiListdto = new APIListDTO();
        if (model != null) {
            APIDTO apidto = new APIDTO();
            apidto.setApiId(model.getApiId());
            apidto.setVersion(model.getVersion());
            apidto.setContext(model.getContext());
            apidto.setPolicy(model.getPolicy());
            apidto.setProvider(model.getProvider());
            apidto.setApiType(model.getApiType());
            apidto.setName(model.getName());
            apidto.setIsDefaultVersion(model.isDefaultVersion());
            Map<String,URLMapping> urlMappings = model.getAllResources();
            List<URLMappingDTO> urlMappingsDTO = new ArrayList<>();
            for (URLMapping urlMapping : urlMappings.values()) {
                URLMappingDTO urlMappingDTO = new URLMappingDTO();
                urlMappingDTO.setAuthScheme(urlMapping.getAuthScheme());
                urlMappingDTO.setHttpMethod(urlMapping.getHttpMethod());
                urlMappingDTO.setThrottlingPolicy(urlMapping.getThrottlingPolicy());
                urlMappingDTO.setUrlPattern(urlMapping.getUrlPattern());
                urlMappingDTO.setScopes(urlMapping.getScopes());
                urlMappingsDTO.add(urlMappingDTO);
            }
            apidto.setUrlMappings(urlMappingsDTO);
            apiListdto.setCount(1);
            apiListdto.getList().add(apidto);
        } else {
            apiListdto.setCount(0);
        }
        return apiListdto;
    }

    public static APIListDTO fromAPIListToAPIListDTO(List<API> apiList) {

        APIListDTO apiListDTO = new APIListDTO();

        if (apiList != null) {
            for (API api : apiList) {
                apiListDTO.getList().add(fromAPItoDTO(api));
            }
            apiListDTO.setCount(apiList.size());
        } else {
            apiListDTO.setCount(0);
        }

        return apiListDTO;
    }

    public static ApplicationListDTO fromApplicationToApplicationListDTO(List<Application> model) {

        ApplicationListDTO applicationListDTO = new ApplicationListDTO();
        if (model != null) {
            for (Application appModel : model) {
                ApplicationDTO applicationDTO = new ApplicationDTO();
                applicationDTO.setUuid(appModel.getUuid());
                applicationDTO.setId(appModel.getId());
                applicationDTO.setName(appModel.getName());
                applicationDTO.setPolicy(appModel.getPolicy());
                applicationDTO.setSubName(appModel.getSubName());
                applicationDTO.setTokenType(appModel.getTokenType());

                Set<String> groupIds = appModel.getGroupIds();
                for (String grp : groupIds) {
                    GroupIdDTO groupIdDTO = new GroupIdDTO();
                    groupIdDTO.setApplicationId(appModel.getId());
                    groupIdDTO.setGroupId(grp);
                    applicationDTO.getGroupIds().add(groupIdDTO);
                }

                Map<String, String> attributes = appModel.getAttributes();
                applicationDTO.setAttributes(attributes);
                applicationListDTO.getList().add(applicationDTO);
            }
            applicationListDTO.setCount(model.size());

        } else {
            applicationListDTO.setCount(0);
        }
        return applicationListDTO;
    }

    public static SubscriptionListDTO fromSubscriptionToSubscriptionListDTO(List<Subscription> model) {

        SubscriptionListDTO subscriptionListDTO = new SubscriptionListDTO();
        if (model != null) {
            for (Subscription subsModel : model) {
                SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
                subscriptionDTO.setApiId(subsModel.getApiId());
                subscriptionDTO.setAppId(subsModel.getAppId());
                subscriptionDTO.setSubscriptionId(subsModel.getSubscriptionId());
                subscriptionDTO.setPolicyId(subsModel.getPolicyId());
                subscriptionDTO.setSubscriptionState(subsModel.getSubscriptionState());

                subscriptionListDTO.getList().add(subscriptionDTO);

            }
            subscriptionListDTO.setCount(model.size());

        } else {
            subscriptionListDTO.setCount(0);
        }
        return subscriptionListDTO;
    }

    public static SubscriptionPolicyListDTO fromSubscriptionPolicyToSubscriptionPolicyListDTO (
            List<SubscriptionPolicy> model) {

        SubscriptionPolicyListDTO subscriptionPolicyListDTO = new SubscriptionPolicyListDTO();
        if (model != null) {
            for (SubscriptionPolicy subscriptionPolicyModel : model) {
                SubscriptionPolicyDTO subscriptionPolicyDTO = new SubscriptionPolicyDTO();
                subscriptionPolicyDTO.setId(subscriptionPolicyModel.getId());
                subscriptionPolicyDTO.setName(subscriptionPolicyModel.getName());
                subscriptionPolicyDTO.setQuotaType(subscriptionPolicyModel.getQuotaType());
                subscriptionPolicyDTO.setGraphQLMaxDepth(subscriptionPolicyModel.getGraphQLMaxDepth());
                subscriptionPolicyDTO.setGraphQLMaxComplexity(subscriptionPolicyModel.getGraphQLMaxComplexity());
                subscriptionPolicyDTO.setTenantId(subscriptionPolicyModel.getTenantId());
                subscriptionPolicyDTO.setRateLimitCount(subscriptionPolicyModel.getRateLimitCount());
                subscriptionPolicyDTO.setStopOnQuotaReach(subscriptionPolicyModel.isStopOnQuotaReach());
                subscriptionPolicyDTO.setRateLimitTimeUnit(subscriptionPolicyModel.getRateLimitTimeUnit());
                subscriptionPolicyDTO.setDefaultLimit(getThrottleLimitDTO(subscriptionPolicyModel));
                subscriptionPolicyListDTO.getList().add(subscriptionPolicyDTO);

            }
            subscriptionPolicyListDTO.setCount(model.size());

        } else {
            subscriptionPolicyListDTO.setCount(0);
        }
        return subscriptionPolicyListDTO;
    }

    public static ThrottleLimitDTO getThrottleLimitDTO(Policy policy) {

        QuotaPolicy quotaPolicy = policy.getQuotaPolicy();
        ThrottleLimitDTO defaultLimit = new ThrottleLimitDTO();
        defaultLimit.setQuotaType(quotaPolicy.getType());
        if (PolicyConstants.REQUEST_COUNT_TYPE.equals(quotaPolicy.getType())) {
            RequestCountLimit requestCountLimit = (RequestCountLimit) quotaPolicy.getLimit();
            defaultLimit.setRequestCount(fromRequestCountLimitToDTO(requestCountLimit));
        } else if (PolicyConstants.BANDWIDTH_TYPE.equals(quotaPolicy.getType())) {
            BandwidthLimit bandwidthLimit = (BandwidthLimit) quotaPolicy.getLimit();
            defaultLimit.setBandwidth(fromBandwidthLimitToDTO(bandwidthLimit));
        }
        return defaultLimit;
    }

    public static ThrottleLimitDTO getThrottleLimitDTO(APIPolicyConditionGroup apiPolicyConditionGroup) {

        QuotaPolicy quotaPolicy = apiPolicyConditionGroup.getQuotaPolicy();
        if (quotaPolicy != null) {
            ThrottleLimitDTO defaultLimit = new ThrottleLimitDTO();
            defaultLimit.setQuotaType(quotaPolicy.getType());
            if (PolicyConstants.REQUEST_COUNT_TYPE.equals(quotaPolicy.getType())) {
                RequestCountLimit requestCountLimit = (RequestCountLimit) quotaPolicy.getLimit();
                defaultLimit.setRequestCount(fromRequestCountLimitToDTO(requestCountLimit));
            } else if (PolicyConstants.BANDWIDTH_TYPE.equals(quotaPolicy.getType())) {
                BandwidthLimit bandwidthLimit = (BandwidthLimit) quotaPolicy.getLimit();
                defaultLimit.setBandwidth(fromBandwidthLimitToDTO(bandwidthLimit));
            }
            return defaultLimit;
        }
        return null;
    }

    /**
     * Converts a Bandwidth Limit model object into a Bandwidth Limit DTO object
     *
     * @param bandwidthLimit Bandwidth Limit model object
     * @return Bandwidth Limit DTO object derived from model
     */
    public static BandwidthLimitDTO fromBandwidthLimitToDTO(BandwidthLimit bandwidthLimit) {

        BandwidthLimitDTO dto = new BandwidthLimitDTO();
        dto.setTimeUnit(bandwidthLimit.getTimeUnit());
        dto.setUnitTime(bandwidthLimit.getUnitTime());
        dto.setDataAmount(bandwidthLimit.getDataAmount());
        dto.setDataUnit(bandwidthLimit.getDataUnit());
        return dto;
    }

    /**
     * Converts a Request Count Limit model object into a Request Count Limit DTO object
     *
     * @param requestCountLimit Request Count Limit model object
     * @return Request Count DTO object derived from model
     */
    public static RequestCountLimitDTO fromRequestCountLimitToDTO(RequestCountLimit requestCountLimit) {

        RequestCountLimitDTO dto = new RequestCountLimitDTO();
        dto.setTimeUnit(requestCountLimit.getTimeUnit());
        dto.setUnitTime(requestCountLimit.getUnitTime());
        dto.setRequestCount(requestCountLimit.getRequestCount());
        return dto;
    }

    public static ApplicationPolicyListDTO fromApplicationPolicyToApplicationPolicyListDTO(List<ApplicationPolicy> model) {

        ApplicationPolicyListDTO applicationPolicyListDTO = new ApplicationPolicyListDTO();
        if (model != null) {
            for (ApplicationPolicy applicationPolicyModel : model) {
                ApplicationPolicyDTO applicationPolicyDTO = new ApplicationPolicyDTO();
                applicationPolicyDTO.setId(applicationPolicyModel.getId());
                applicationPolicyDTO.setName(applicationPolicyModel.getName());
                applicationPolicyDTO.setQuotaType(applicationPolicyModel.getQuotaType());
                applicationPolicyDTO.setTenantId(applicationPolicyModel.getTenantId());
                applicationPolicyDTO.setDefaultLimit(getThrottleLimitDTO(applicationPolicyModel));

                applicationPolicyListDTO.getList().add(applicationPolicyDTO);

            }
            applicationPolicyListDTO.setCount(model.size());

        } else {
            applicationPolicyListDTO.setCount(0);
        }
        return applicationPolicyListDTO;
    }

    public static ApiPolicyListDTO fromApiPolicyToApiPolicyListDTO(List<APIPolicy> model) {

        ApiPolicyListDTO apiPolicyListDTO = new ApiPolicyListDTO();
        if (model != null) {
            for (APIPolicy apiPolicyModel : model) {
                ApiPolicyDTO policyDTO = new ApiPolicyDTO();
                policyDTO.setName(apiPolicyModel.getName());
                policyDTO.setQuotaType(apiPolicyModel.getQuotaType());
                policyDTO.setTenantId(apiPolicyModel.getTenantId());
                policyDTO.setApplicableLevel(apiPolicyModel.getApplicableLevel());
                policyDTO.setDefaultLimit(getThrottleLimitDTO(apiPolicyModel));
                apiPolicyListDTO.getList().add(policyDTO);

                List<APIPolicyConditionGroup> retrievedGroups = apiPolicyModel.getConditionGroups();
                List<ApiPolicyConditionGroupDTO> condGroups = new ArrayList<ApiPolicyConditionGroupDTO>();
                for (APIPolicyConditionGroup retGroup : retrievedGroups) {
                    ApiPolicyConditionGroupDTO group = new ApiPolicyConditionGroupDTO();
                    group.setConditionGroupId(retGroup.getConditionGroupId());
                    group.setQuotaType(retGroup.getQuotaType());
                    group.setDefaultLimit(getThrottleLimitDTO(retGroup));
                    group.setPolicyId(retGroup.getPolicyId());

                    List<org.wso2.carbon.apimgt.internal.service.dto.ConditionDTO> condition = 
                            new ArrayList<org.wso2.carbon.apimgt.internal.service.dto.ConditionDTO>();

                    List<ConditionDTO> retrievedConditions = retGroup.getConditionDTOS();
                    for (ConditionDTO retrievedCondition : retrievedConditions) {
                        org.wso2.carbon.apimgt.internal.service.dto.ConditionDTO conditionDTO = 
                                new org.wso2.carbon.apimgt.internal.service.dto.ConditionDTO();
                        conditionDTO.setConditionType(retrievedCondition.getConditionType());
                        conditionDTO.setIsInverted(retrievedCondition.isInverted());
                        conditionDTO.setName(retrievedCondition.getConditionName());
                        conditionDTO.setValue(retrievedCondition.getConditionValue());
                        condition.add(conditionDTO);
                    }
                    group.setCondition(condition);
                    condGroups.add(group);
                }
                policyDTO.setConditionGroups(condGroups);
            }
            apiPolicyListDTO.setCount(model.size());
        } else {
            apiPolicyListDTO.setCount(0);
        }
        return apiPolicyListDTO;
    }

    public static ApplicationKeyMappingListDTO fromApplicationKeyMappingToApplicationKeyMappingListDTO(
            List<ApplicationKeyMapping> model) {

        ApplicationKeyMappingListDTO applicationKeyMappingListDTO = new ApplicationKeyMappingListDTO();
        if (model != null) {
            for (ApplicationKeyMapping applicationKeyMapping : model) {
                ApplicationKeyMappingDTO applicationKeyMappingDTO = new ApplicationKeyMappingDTO();
                applicationKeyMappingDTO.setApplicationId(applicationKeyMapping.getApplicationId());
                applicationKeyMappingDTO.setConsumerKey(applicationKeyMapping.getConsumerKey());
                applicationKeyMappingDTO.setKeyType(applicationKeyMapping.getKeyType());
                applicationKeyMappingDTO.setKeyManager(applicationKeyMapping.getKeyManager());
                applicationKeyMappingListDTO.getList().add(applicationKeyMappingDTO);

            }
            applicationKeyMappingListDTO.setCount(model.size());

        } else {
            applicationKeyMappingListDTO.setCount(0);
        }
        return applicationKeyMappingListDTO;
    }

    public static String validateTenantDomain(String xWSO2Tenant, MessageContext messageContext) {

        String tenantDomain = RestApiUtil.getLoggedInUserTenantDomain();
        if (xWSO2Tenant == null) {
            return tenantDomain;
        } else {
            if (MultitenantConstants.SUPER_TENANT_DOMAIN_NAME.equals(tenantDomain)) {
                return xWSO2Tenant;
            } else {
                return tenantDomain;
            }
        }

    }

    public static ScopesListDTO fromScopeListToScopeDtoList(List<Scope> model) {

        ScopesListDTO scopesListDTO = new ScopesListDTO();
        List<ScopeDTO> scopeDTOList = new ArrayList<>();
        for (Scope scope : model) {
            scopeDTOList.add(fromScopeToScopeDto(scope));
        }
        scopesListDTO.setList(scopeDTOList);
        scopesListDTO.setCount(scopeDTOList.size());
        return scopesListDTO;
    }

    private static ScopeDTO fromScopeToScopeDto(Scope scope) {
        ScopeDTO scopeDTO = new ScopeDTO();
        scopeDTO.setName(scope.getKey());
        scopeDTO.setDisplayName(scope.getName());
        scopeDTO.setDescription(scope.getDescription());
        String roles = scope.getRoles();
        if (StringUtils.isNotEmpty(roles) && roles.trim().length() > 0) {
            scopeDTO.setRoles(Arrays.asList(roles.split(",")));
        }
        return scopeDTO;
    }
}
