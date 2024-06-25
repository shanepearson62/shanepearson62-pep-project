package Service;

import java.util.List;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;
    
    public AccountService(){
        accountDAO = new AccountDAO();
    }
    
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }
    
    public List<Account> getAllAccounts() {
        return accountDAO.getAllAccounts();
    }

    public boolean existsByUsername(String username) {
        return accountDAO.existsByUsername(username);
    }

    public Account createAccount(String username, String password) {
        return accountDAO.createAccount(username, password);
    }

    public Account findByUsernameAndPassword(String username, String password) {
        return accountDAO.findByUsernameAndPassword(username, password);
    }

    public boolean accountExistsByID(int posted_by) {
        return accountDAO.accountExistsByID(posted_by);
    }
}
