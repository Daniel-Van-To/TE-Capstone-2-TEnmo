package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService(API_BASE_URL);
    private final TransferService transferService = new TransferService(API_BASE_URL);

    private AuthenticatedUser currentUser;
    private final RestTemplate restTemplate =new RestTemplate();
    private HttpHeaders headers = new HttpHeaders();

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
        User user = currentUser.getUser();
        Account account = accountService.getAccount(user.getId());

        consoleService.printBalance(account.getBalance());

		
	}

	private void viewTransferHistory() {
		// TODO Auto-generated method stub
        User user = currentUser.getUser();
        Transfer[] transfer = transferService.listTransfers(user.getId());

        consoleService.printTransferList(transfer);
		
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		User user = currentUser.getUser();
        Transfer[] requests = transferService.listPendingRequests(user.getId());
        List<Transfer> testList = new ArrayList<>(Arrays.asList(requests));
        if (testList.size() > 0 && testList.get(0) != null) {
            consoleService.printTransferList(requests);
            int userInput = consoleService.promptForInt("Enter Transfer Id to approve or reject a request: ");
            Transfer selectedTransfer = transferService.getTransferById(userInput, requests);

            System.out.println(selectedTransfer);
            consoleService.printApproveOrRejectMenu();
            int selectedOption = consoleService.promptForInt("Please choose an option: ");

            if (selectedOption == 1) {
                selectedTransfer.setTransferStatusId(2);
                transferService.updatedTransfer(selectedTransfer);
            } else if (selectedOption == 2) {
                selectedTransfer.setTransferStatusId(3);
                transferService.updatedTransfer(selectedTransfer);
            } else if (selectedOption == 0) {
                selectedTransfer.setTransferStatusId(1);
                transferService.updatedTransfer(selectedTransfer);
            }
        }else {
            System.out.println("No pending requests.");
        }

        mainMenu();

	}

	private void sendBucks() {
		// TODO Auto-generated method stub
		
	}

	private void requestBucks() {
		// TODO Auto-generated method stub


	}

    private void approveRequest() {
        // TODO
    }

}
