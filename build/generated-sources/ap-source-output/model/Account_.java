package model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.operation.builder.BankingOperation;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Account.class)
public abstract class Account_ {

	public static volatile ListAttribute<Account, BankingOperation> m_operationList;
	public static volatile SingularAttribute<Account, Long> m_number;
	public static volatile SingularAttribute<Account, Byte> m_type;
	public static volatile SingularAttribute<Account, String> m_typeDescription;
	public static volatile SingularAttribute<Account, Double> m_value;

}

