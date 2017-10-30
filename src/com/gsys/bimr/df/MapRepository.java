package com.gsys.bimr.df;

import com.gsys.bimr.df.model.TwitterRequest;
import com.gsys.bimr.df.model.TwitterResponse;

public interface MapRepository {

	TwitterResponse retrieveTweets(TwitterRequest request);

}
