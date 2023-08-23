package Service;
import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    private AccountDAO accountDAO;
    public AccountService () {
        accountDAO = new AccountDAO();
    }
    public Account registerUser(Account account) {
        return accountDAO.registerUser(account);
    }
    public Account login(String username, String password) {
        return accountDAO.login(username, password);
    }
}
