package com.bank.domain.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import com.bank.domain.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity(name="transactions")
@Table(name="transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private BigDecimal amount;
	
	@ManyToOne
	@JoinColumn(name="sender_id")
	private User sender;
	
	@JoinColumn(name="receiver_id")
	@ManyToOne
	private User receiver;
	private LocalDateTime timestamp;
	
}
// oi :o)
