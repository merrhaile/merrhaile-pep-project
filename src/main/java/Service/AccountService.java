package Service;

import DAO.AccountDAO;
import Model.Account;


/*
 * The service layer serves as a bridge between the controller and the dao
 */
public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }
    /*
     * This service takes an account object supplied by the handler 
     * It invokes the userRegistration from the accounDAO
     * @returns a reristered account
     */
    public Account userRegistration(Account account){
       return accountDAO.userRegistration(account);
    }

    /*
     * Invokes userLogin from accountDAO
     * returns account object
     */
    public Account userLogin(Account account){
       return accountDAO.userLogin(account);
    }
}
