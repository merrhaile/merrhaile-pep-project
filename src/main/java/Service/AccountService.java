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
        if(account.getUsername().length() == 0)  {
            return null;
        }
       else if(account.getPassword().length() < 4) {
            return null;
        }
       else if(accountDAO.userLogin(account) != null){
            return null;
        }
       else return accountDAO.userRegistration(account);
    }

    /*
     * Invokes userLogin from accountDAO
     * returns account object
     */
    public Account userLogin(Account account){
        Account userDetail = accountDAO.userLogin(account);
        if(userDetail == null) return null;
        if(account.getUsername().length() == 0) return null;
        if(userDetail.getPassword() != account.getPassword()) return null;
        System.out.println(userDetail);
       return userDetail;
    }
}
