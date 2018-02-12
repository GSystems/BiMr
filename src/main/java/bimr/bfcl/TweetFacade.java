package bimr.bfcl;

import java.util.Date;
import java.util.List;

import bimr.bfcl.dto.TweetDTO;
import bimr.bfcl.dto.TweetRequestDTO;
import bimr.bfcl.dto.TweetResponseDTO;

/**
 * @author GLK
 */
public interface TweetFacade {

    /**
     * Retrieve tweets from database
     *
     * @return
     */
    List<TweetDTO> retrieveTweetsFromDB();

    /**
     * Retrieves tweets from twitter api and persist them into database
     *
     * @param request
     * @return
     */
    TweetResponseDTO retrieveTweetsFromApi(TweetRequestDTO request);

    /**
     * Insert a list of tweets into database
     *
     * @param tweets
     */
    void persistTweetsInRelationalDb(List<TweetDTO> tweets);

    /**
     * Retrieve the most recent tweet id from the database
     */
    Long retrieveLastTweetId();

    /**
     * Retrieve the MIN Date from the database
     */
    Date retrieveMinDateOfTweets();

}
