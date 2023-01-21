package com.resteam;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.services.personalizeruntime.PersonalizeRuntimeClient;
import software.amazon.awssdk.services.personalizeruntime.model.GetRecommendationsRequest;
import software.amazon.awssdk.services.personalizeruntime.model.GetRecommendationsResponse;
import software.amazon.awssdk.services.personalizeruntime.model.PredictedItem;

import java.util.List;

public class PersonalizeRecommendations {
    public static void getRecs(PersonalizeRuntimeClient personalizeRuntimeClient, String campaignArn, String userId){

        try {
            GetRecommendationsRequest recommendationsRequest = GetRecommendationsRequest.builder()
                    .campaignArn(campaignArn)
                    .numResults(5)
                    .userId(userId)
                    .itemId("ITEM_ID")
                    .build();

            GetRecommendationsResponse recommendationsResponse = personalizeRuntimeClient.getRecommendations(recommendationsRequest);
            List<PredictedItem> items = recommendationsResponse.itemList();
            for (PredictedItem item: items) {
                System.out.println("Item Id is : " + item.itemId());
                System.out.println("Item score is : " + item.score());
            }
            System.out.println("Recommendation ID: " + recommendationsResponse.recommendationId());

        } catch (AwsServiceException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
}
