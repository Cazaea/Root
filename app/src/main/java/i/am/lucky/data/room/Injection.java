package i.am.lucky.data.room;

import i.am.lucky.utils.AppExecutors;

/**
 * @author Cazaea
 * @data 2018/4/19
 * @Description
 */

public class Injection {

    public static UserDataBaseSource get() {
        UserDataBase database = UserDataBase.getDatabase();
        return UserDataBaseSource.getInstance(new AppExecutors(), database.waitDao());
    }

}
