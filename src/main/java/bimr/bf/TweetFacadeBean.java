package bimr.bf;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import bimr.bf.transformer.MapTransformer;
import bimr.bfcl.TweetFacade;
import bimr.bfcl.dto.TweetDTO;
import bimr.df.TweetRepo;
import bimr.util.AsyncUtils;

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
