package main.java.df;

import java.util.List;

import javax.inject.Inject;

import main.java.df.mapper.MapMapper;
import main.java.df.model.EBirdRequest;
import main.java.df.model.EBirdResponse;
import main.java.df.model.Tweet;
import main.java.df.model.TwitterRequest;
import main.java.df.model.TwitterResponse;
import main.java.rf.ebird.EbirdsServiceClient;
import main.java.rf.ebird.EbirdsServiceClientBean;
import main.java.rf.twitter.TwitterServiceClientBean;
//import main.java.rf.twitter.dao.TwitterDAO;

public class MapRepositoryBean implements MapRepository {

	@Inject
	private TwitterServiceClientBean twitterService;
	
	@Inject
	private EbirdsServiceClient ebirdsService;
	
//	@Inject
//	private TwitterDAO twitterDAO;

	@Override
	public TwitterResponse retrieveTweets(TwitterRequest request) {
		twitterService = new TwitterServiceClientBean();
		return MapMapper.toTwitterResponseFromWrapper(
				twitterService.retrieveTweets(MapMapper.fromTwitterRequestToWrapper(request)));
	}

	@Override
	public void insertTweets(List<Tweet> tweets) {
//		for(Tweet tweet : tweets) {
//			twitterDAO.insertTweet(MapMapper.fromTweetToEntity(tweet));
//		}
	}

	@Override
	public EBirdResponse retrieveEBirdData(EBirdRequest request) {
		ebirdsService = new EbirdsServiceClientBean();
		return MapMapper.toEbirdsResponseFromWrapper(
				ebirdsService.retrieveEBirdData(MapMapper.fromEBirdRequestToWrapper(request)));
	}

}
