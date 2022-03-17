package cse.netsys.drftclient.util;

import cse.netsys.drftclient.api.DRFTAPIService;

public class APIClient {
    public static DRFTAPIService getAPIClient(String url) {
        return RetrofitClient.getRetrofitClient(url).create(DRFTAPIService.class);
    }
}
