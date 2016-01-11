import com.bid.data.Item;
import com.bid.data.User;
import com.bid.service.BidService;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertTrue;

/**
 * Created by skupunarapu on 1/8/2016.
 */
public class TestApp {

    private BidService bidService;
    private static final String[] CLOSING_TIMES = {"2016-01-08T12:34", "2016-01-09T16:34",
            "2016-01-10T11:33", "2016-12-25T13:34", "2016-03-02T20:34"};

    private static final int USER_LIST_SIZE = 2;
    private static final int CREATE_TIMES = 2;
    private static final int LIST_TIMES = 2;
    private static final int INFO_TIMES = 2;
    private static final int BID_TIMES = 2;


    @Test
    public void testCreationBidInfo() throws Exception {
        bidService = new BidService();
        Random random = new Random();
        List<User> userList = new ArrayList<User>(USER_LIST_SIZE);

        int id = (random).nextInt(10000);
        for (int i = 0; i < USER_LIST_SIZE; i++) {
            userList.add(new User("fakeUser" + id, false));
        }

        List<Long> auctionIds = new ArrayList<Long>();
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);

            for (int j = 0; j < CREATE_TIMES; j++) {

                String title = "The title " + random.nextInt(10000);
                String description = "The description " + random.nextInt(10000);
                double minValue = random.nextDouble() * 10000;
                String closingTimeString = CLOSING_TIMES[random.nextInt(CLOSING_TIMES.length)];

                Item item = new Item();
                item.setItemValue(minValue);
                item.setItemName(title);
                item.setItemDescription(description);

                long auctionId = bidService.createAuction(item, closingTimeString, user);

                assertTrue(auctionId != -1);

                auctionIds.add(auctionId);
            }

            for (int j =0; j < BID_TIMES; j++) {
                long auctionId = auctionIds.get(random.nextInt(auctionIds.size()));
                double bid = random.nextDouble() * 10000;
                boolean result = bidService.bid(user, auctionId, bid);

                assertTrue(result);
            }


        }
    }
}
