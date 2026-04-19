package com.payflow.payment.application.required;

import java.util.function.Supplier;

/**
 * @description    :
 */
public interface PaymentLockManager {
	<T> T executeWithLock(Long memberId, Supplier<T> action);
}
