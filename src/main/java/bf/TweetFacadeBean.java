package main.java.bf;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import main.java.bf.transformer.MapTransformer;
import main.java.bfcl.TweetFacade;
import main.java.bfcl.dto.TweetDTO;
import main.java.df.TweetRepo;
import main.java.util.AsyncUtils;

/**
 * @author GLK
 */
@Stateless
public class TweetFacadeBean implements TweetFacade {

	@Inject
	private TweetRepo repo;

	@Override
	public List<TweetDTO> retrieveTweetsFromDB() {
		return MapTransformer.fromTweetsToDTO(AsyncUtils.getResultFromAsyncTask(repo.retrieveTweetsFromDB()));
	}

}
