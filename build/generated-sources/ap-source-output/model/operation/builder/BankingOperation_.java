package model.operation.builder;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Account;
import model.Client;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(BankingOperation.class)
public abstract class BankingOperation_ {

	public static volatile SingularAttribute<BankingOperation, Long> m_operationId;
	public static volatile SingularAttribute<BankingOperation, Account> m_currentAccount;
	public static volatile SingularAttribute<BankingOperation, String> m_name;
	public static volatile SingularAttribute<BankingOperation, Long> m_date;
	public static volatile SingularAttribute<BankingOperation, Integer> m_operationType;
	public static volatile SingularAttribute<BankingOperation, String> m_operationTypeDescription;
	public static volatile SingularAttribute<BankingOperation, Double> m_beforeOperationClientBalance;
	public static volatile SingularAttribute<BankingOperation, Client> m_client;

}

