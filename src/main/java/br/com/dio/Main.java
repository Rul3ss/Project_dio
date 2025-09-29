package br.com.dio;

import br.com.dio.expcetion.AccountNotFoundException;
import br.com.dio.expcetion.NoFundsEnoughException;
import br.com.dio.expcetion.WalletNotFoundException;
import br.com.dio.model.AccountWallet;
import br.com.dio.repository.AccountRepository;
import br.com.dio.repository.InvestmentRepository;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    private final static AccountRepository accountRepository = new AccountRepository();
    private final static InvestmentRepository investmentRepository = new InvestmentRepository();

    public static void main(String[] args) {
        System.out.println("Olá, seja bem vindo ao DIO Bank");
        while(true){
            System.out.println("Selecione a operação desejada");
            System.out.println("1 - Criar uma conta");
            System.out.println("2 - Criar um investimento");
            System.out.println("3 - Criar uma carteira de investimento investimento");
            System.out.println("4 - Depositar na conta");
            System.out.println("5 - Sacar da conta");
            System.out.println("6 - Transferência entre contas");
            System.out.println("7 - Investir");
            System.out.println("8 - Sacar Investimento");
            System.out.println("9 - Listar Contas");
            System.out.println("10 - Listar Investimentos");
            System.out.println("11 - Listar Carteiras de Investimentos");
            System.out.println("12 - Atualizar Investimentos");
            System.out.println("13 - Histórico de contas");
            System.out.println("14 - Sair ");
            var option = scanner.nextInt();
            scanner.nextLine();
            switch(option){
                case 1:
                    createAccount();
                    break;
                case 2:
                    createInvestment();
                    break;
                case 3:
                    createWalletInvestment();
                    break;
                case 4:
                    deposit();
                    break;
                case 5:
                    withdraw();
                    break;
                case 6:
                    transferToAccount();
                    break;
                case 7:
                    incInvestment();
                    break;
                case 8:
                    rescueInvestment();
                    break;
                case 9:
                    accountRepository.list().forEach(System.out::println);
                    break;
                case 10:
                    investmentRepository.list().forEach(System.out::println);
                    break;
                case 11:
                    investmentRepository.listWallets().forEach(System.out::println);
                    break;
                case 12:
                    investmentRepository.updateAmount();
                    System.out.println("Investimentos reajustado");

                break;
                case 13:
                    checkHistory();
                    break;
                case 14:
                    System.exit(0);
                default: System.out.println("Opção Invalida");
            }

        }
    }

    private static void createAccount(){
        System.out.println("Informe as chaves pix (separadas por ';'");
        var pix = Arrays.stream(scanner.nextLine().split(";")).toList();
        System.out.println("Informe o valor inicial de Deposito");
        var amount = scanner.nextLong();
        var wallet = accountRepository.create(pix, amount);
        System.out.println("Conta criada" + wallet);
    }

    private static void createInvestment(){
        System.out.println("Informe a taxa do investimento");
        var tax = scanner.nextInt();
        System.out.println("Informe o valor inicial de Deposito");
        var initialFunds = scanner.nextLong();
        var investment = investmentRepository.create(tax, initialFunds);
        System.out.println("Conta criada" + investment);
    }

    private static void deposit(){
        System.out.println("Informe a chave pix da conta para deposito");
        var pix = scanner.next();
        System.out.println("Informe o valor que será depositado");
        var amount = scanner.nextLong();
        try {
            accountRepository.deposit(pix, amount);
        } catch ( NoFundsEnoughException | AccountNotFoundException ex){
            System.out.println(ex.getMessage());
        }
    }

    private  static void withdraw(){
        System.out.println("Informe a chave pix da conta para saque");
        var pix = scanner.next();
        System.out.println("Informe o valor que será sacado");
        var amount = scanner.nextLong();
        try {
            accountRepository.withdraw(pix, amount);
        } catch (AccountNotFoundException ex){
            System.out.println(ex.getMessage());
        }
    }
    private static void transferToAccount(){
        System.out.println("Informe a chave pix da conta de origem");
        var source = scanner.next();
        System.out.println("Informe o pix da conta de destino");
        var target = scanner.next();
        System.out.println("Informe o valor desejado");
        var amount = scanner.nextLong();
        try {
            accountRepository.transferMoney(source,target, amount);
        } catch ( NoFundsEnoughException | AccountNotFoundException ex){
            System.out.println(ex.getMessage());
        }
    }

    private static void createWalletInvestment(){
        System.out.println("Informe a chave pix da conta");
        var pix = scanner.next();
        var account = accountRepository.findByPix(pix);
        System.out.println("Informe o identificador do investimento");
        var investmentId = scanner.nextInt();
        var investmentWallet = investmentRepository.initInvestment(account,investmentId);
        System.out.println("Conta de investimento criada " + investmentWallet);
    }

    public static void incInvestment(){
        System.out.println("Informe a chave pix da conta");
        var pix = scanner.next();
        System.out.println("Informe o valor de investimento");
        var amount = scanner.nextLong();
        try {
            InvestmentRepository.deposit(pix, amount);
        } catch (WalletNotFoundException | AccountNotFoundException ex){
            System.out.println(ex.getMessage());
        }

    }
    private  static void rescueInvestment(){
        System.out.println("Informe a chave pix da conta resgate do Investment");
        var pix = scanner.next();
        System.out.println("Informe o valor que será sacado");
        var amount = scanner.nextLong();
        try {
            investmentRepository.withdraw(pix, amount);
        } catch (AccountNotFoundException ex){
            System.out.println(ex.getMessage());
        }
    }

    private static void checkHistory(){
        System.out.println("Informe a chave pix da conta para Verificar o extrato");
        var pix = scanner.next();
        AccountWallet wallet;
        try{
         var sortedHistory =  accountRepository.getHistory(pix);
         sortedHistory.forEach((k,v) -> {System.out.println(k.format(DateTimeFormatter.ISO_DATE_TIME));
         System.out.println(v.getFirst().transactionId());
         System.out.println(v.getFirst().description());
             System.out.println("R$" + (v.size()/100) +"," + (v.size()%100));

         });

        } catch (AccountNotFoundException ex){
            System.out.println(ex.getMessage());
        }

    }

}
