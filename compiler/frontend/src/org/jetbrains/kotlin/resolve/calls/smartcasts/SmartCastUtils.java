/*
 * Copyright 2010-2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.resolve.calls.smartcasts;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import kotlin.KotlinPackage;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.kotlin.descriptors.DeclarationDescriptor;
import org.jetbrains.kotlin.descriptors.ReceiverParameterDescriptor;
import org.jetbrains.kotlin.psi.JetExpression;
import org.jetbrains.kotlin.resolve.BindingContext;
import org.jetbrains.kotlin.resolve.BindingTrace;
import org.jetbrains.kotlin.resolve.calls.ArgumentTypeResolver;
import org.jetbrains.kotlin.resolve.calls.context.ResolutionContext;
import org.jetbrains.kotlin.resolve.scopes.receivers.ExpressionReceiver;
import org.jetbrains.kotlin.resolve.scopes.receivers.ReceiverValue;
import org.jetbrains.kotlin.resolve.scopes.receivers.ThisReceiver;
import org.jetbrains.kotlin.types.JetType;
import org.jetbrains.kotlin.types.TypeUtils;
import org.jetbrains.kotlin.types.checker.JetTypeChecker;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.jetbrains.kotlin.diagnostics.Errors.SMARTCAST_IMPOSSIBLE;
import static org.jetbrains.kotlin.resolve.BindingContext.SMARTCAST;

public class SmartCastUtils {

    private SmartCastUtils() {}

    @NotNull
    public static List<JetType> getSmartCastVariants(
            @NotNull ReceiverValue receiverToCast,
            @NotNull ResolutionContext context
    ) {
        return getSmartCastVariants(receiverToCast, context.trace.getBindingContext(), context.scope.getContainingDeclaration(), context.dataFlowInfo);
    }

    @NotNull
    public static List<JetType> getSmartCastVariants(
            @NotNull ReceiverValue receiverToCast,
            @NotNull BindingContext bindingContext,
            @NotNull DeclarationDescriptor containingDeclarationOrModule,
            @NotNull DataFlowInfo dataFlowInfo
    ) {
        List<JetType> variants = Lists.newArrayList();
        variants.add(receiverToCast.getType());
        variants.addAll(getSmartCastVariantsExcludingReceiver(bindingContext, containingDeclarationOrModule, dataFlowInfo, receiverToCast));
        return variants;
    }

    @NotNull
    public static List<JetType> getSmartCastVariantsWithLessSpecificExcluded(
            @NotNull ReceiverValue receiverToCast,
            @NotNull BindingContext bindingContext,
            @NotNull DeclarationDescriptor containingDeclarationOrModule,
            @NotNull DataFlowInfo dataFlowInfo
    ) {
        final List<JetType> variants = getSmartCastVariants(receiverToCast, bindingContext,
                                                            containingDeclarationOrModule, dataFlowInfo);
        return KotlinPackage.filter(variants, new Function1<JetType, Boolean>() {
            @Override
            public Boolean invoke(final JetType type) {
                return !KotlinPackage.any(variants, new Function1<JetType, Boolean>() {
                    @Override
                    public Boolean invoke(JetType another) {
                        return another != type && JetTypeChecker.DEFAULT.isSubtypeOf(another, type);
                    }
                });
            }
        });
    }

    /**
     * @return variants @param receiverToCast may be cast to according to context dataFlowInfo, receiverToCast itself is NOT included
     */
    @NotNull
    public static Collection<JetType> getSmartCastVariantsExcludingReceiver(
            @NotNull ResolutionContext context,
            @NotNull ReceiverValue receiverToCast
    ) {
        return getSmartCastVariantsExcludingReceiver(context.trace.getBindingContext(),
                                                     context.scope.getContainingDeclaration(),
                                                     context.dataFlowInfo,
                                                     receiverToCast);
    }

    /**
     * @return variants @param receiverToCast may be cast to according to @param dataFlowInfo, @param receiverToCast itself is NOT included
     */
    @NotNull
    public static Collection<JetType> getSmartCastVariantsExcludingReceiver(
            @NotNull BindingContext bindingContext,
            @NotNull DeclarationDescriptor containingDeclarationOrModule,
            @NotNull DataFlowInfo dataFlowInfo,
            @NotNull ReceiverValue receiverToCast
    ) {
        DataFlowValue dataFlowValue = getDataFlowValueExcludingReceiver(bindingContext, containingDeclarationOrModule, receiverToCast);

        if (dataFlowValue != null) {
            return dataFlowInfo.getPossibleTypes(dataFlowValue);
        }

        return Collections.emptyList();
    }

    @Nullable
    public static DataFlowValue getDataFlowValueExcludingReceiver(
            @NotNull BindingContext bindingContext,
            @NotNull DeclarationDescriptor containingDeclarationOrModule,
            @NotNull ReceiverValue receiverToCast
    ) {
        if (receiverToCast instanceof ThisReceiver) {
            ThisReceiver receiver = (ThisReceiver) receiverToCast;
            assert receiver.exists();
            return DataFlowValueFactory.createDataFlowValue(receiver);
        }
        else if (receiverToCast instanceof ExpressionReceiver) {
            return DataFlowValueFactory.createDataFlowValue(
                    receiverToCast, bindingContext, containingDeclarationOrModule
            );
        }
        return null;
    }

    public static boolean isSubTypeBySmartCastIgnoringNullability(
            @NotNull ReceiverValue receiverArgument,
            @NotNull JetType receiverParameterType,
            @NotNull ResolutionContext context
    ) {
        List<JetType> smartCastTypes = getSmartCastVariants(receiverArgument, context);
        return getSmartCastSubType(TypeUtils.makeNullable(receiverParameterType), smartCastTypes) != null;
    }

    @Nullable
    private static JetType getSmartCastSubType(
            @NotNull JetType receiverParameterType,
            @NotNull Collection<JetType> smartCastTypes
    ) {
        Set<JetType> subTypes = Sets.newHashSet();
        for (JetType smartCastType : smartCastTypes) {
            if (ArgumentTypeResolver.isSubtypeOfForArgumentType(smartCastType, receiverParameterType)) {
                subTypes.add(smartCastType);
            }
        }
        if (subTypes.isEmpty()) return null;

        JetType intersection = TypeUtils.intersect(JetTypeChecker.DEFAULT, subTypes);
        if (intersection == null || !intersection.getConstructor().isDenotable()) {
            return receiverParameterType;
        }
        return intersection;
    }

    // Returns false when we need smart cast but cannot do it, otherwise true
    public static boolean recordSmartCastIfNecessary(
            @NotNull ReceiverValue receiver,
            @NotNull JetType receiverParameterType,
            @NotNull ResolutionContext context,
            boolean safeAccess
    ) {
        if (!(receiver instanceof ExpressionReceiver)) return true;

        receiverParameterType = safeAccess ? TypeUtils.makeNullable(receiverParameterType) : receiverParameterType;
        if (ArgumentTypeResolver.isSubtypeOfForArgumentType(receiver.getType(), receiverParameterType)) {
            return true;
        }

        Collection<JetType> smartCastTypesExcludingReceiver = getSmartCastVariantsExcludingReceiver(context, receiver);
        JetType smartCastSubType = getSmartCastSubType(receiverParameterType, smartCastTypesExcludingReceiver);
        if (smartCastSubType == null) return true;

        JetExpression expression = ((ExpressionReceiver) receiver).getExpression();
        DataFlowValue dataFlowValue = DataFlowValueFactory.createDataFlowValue(receiver, context);

        return recordCastOrError(expression, smartCastSubType, context.trace, dataFlowValue.isPredictable(), true);
    }

    public static boolean recordCastOrError(
            @NotNull JetExpression expression,
            @NotNull JetType type,
            @NotNull BindingTrace trace,
            boolean canBeCast,
            boolean recordExpressionType
    ) {
        if (canBeCast) {
            trace.record(SMARTCAST, expression, type);
            if (recordExpressionType) {
                //TODO
                //Why the expression type is rewritten for receivers and is not rewritten for arguments? Is it necessary?
                trace.recordType(expression, type);
            }
        }
        else {
            trace.report(SMARTCAST_IMPOSSIBLE.on(expression, type, expression.getText()));
        }
        return canBeCast;
    }

    public static boolean recordSmartCastToNotNullIfPossible(
            @NotNull ReceiverValue receiver,
            @NotNull ResolutionContext context
    ) {
        if (!TypeUtils.isNullableType(receiver.getType())) return true;

        DataFlowValue dataFlowValue = getDataFlowValueExcludingReceiver(
                context.trace.getBindingContext(),
                context.scope.getContainingDeclaration(),
                receiver
        );

        if (dataFlowValue == null) return false;

        if (!context.dataFlowInfo.getNullability(dataFlowValue).canBeNull()) {
            JetExpression receiverExpression = getReceiverExpression(receiver);

            // report smart cast only on predictable expressions that were not reported before
            if (receiverExpression != null
                && dataFlowValue.isPredictable()
                && context.trace.getBindingContext().get(SMARTCAST, receiverExpression) == null) {
                recordCastOrError(
                        receiverExpression, receiver.getType(), context.trace,
                        /* canBeCast = */ true, /* recordExpressionType = */ false
                );
            }

            return true;
        }

        return !context.dataFlowInfo.getNullability(dataFlowValue).canBeNull();
    }

    @Nullable
    private static JetExpression getReceiverExpression(@NotNull ReceiverValue value) {
        if (value instanceof ExpressionReceiver) {
            return ((ExpressionReceiver) value).getExpression();
        }
        return null;
    }
}
