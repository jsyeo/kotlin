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

package org.jetbrains.kotlin.codegen.generated;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.JUnit3RunnerWithInners;
import org.jetbrains.kotlin.test.JetTestUtils;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.generators.tests.TestsPackage}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("compiler/testData/codegen/boxWithJava")
@TestDataPath("$PROJECT_ROOT")
@RunWith(JUnit3RunnerWithInners.class)
public class BlackBoxWithJavaCodegenTestGenerated extends AbstractBlackBoxCodegenTest {
    public void testAllFilesPresentInBoxWithJava() throws Exception {
        JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/codegen/boxWithJava"), Pattern.compile("^([^\\.]+)$"), true);
    }

    @TestMetadata("annotatedSamFunExpression")
    public void testAnnotatedSamFunExpression() throws Exception {
        String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxWithJava/annotatedSamFunExpression/");
        doTestWithJava(fileName);
    }

    @TestMetadata("annotatedSamLambda")
    public void testAnnotatedSamLambda() throws Exception {
        String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxWithJava/annotatedSamLambda/");
        doTestWithJava(fileName);
    }

    @TestMetadata("classObjectAccessor")
    public void testClassObjectAccessor() throws Exception {
        String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxWithJava/classObjectAccessor/");
        doTestWithJava(fileName);
    }

    @TestMetadata("inline")
    public void testInline() throws Exception {
        String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxWithJava/inline/");
        doTestWithJava(fileName);
    }

    @TestMetadata("platformName")
    public void testPlatformName() throws Exception {
        String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxWithJava/platformName/");
        doTestWithJava(fileName);
    }

    @TestMetadata("compiler/testData/codegen/boxWithJava/annotationsWithKClass")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class AnnotationsWithKClass extends AbstractBlackBoxCodegenTest {
        public void testAllFilesPresentInAnnotationsWithKClass() throws Exception {
            JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/codegen/boxWithJava/annotationsWithKClass"), Pattern.compile("^([^\\.]+)$"), true);
        }

        @TestMetadata("array")
        public void testArray() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxWithJava/annotationsWithKClass/array/");
            doTestWithJava(fileName);
        }

        @TestMetadata("basic")
        public void testBasic() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxWithJava/annotationsWithKClass/basic/");
            doTestWithJava(fileName);
        }

        @TestMetadata("vararg")
        public void testVararg() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxWithJava/annotationsWithKClass/vararg/");
            doTestWithJava(fileName);
        }

    }

    @TestMetadata("compiler/testData/codegen/boxWithJava/builtinStubMethods")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class BuiltinStubMethods extends AbstractBlackBoxCodegenTest {
        public void testAllFilesPresentInBuiltinStubMethods() throws Exception {
            JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/codegen/boxWithJava/builtinStubMethods"), Pattern.compile("^([^\\.]+)$"), true);
        }

        @TestMetadata("extendJavaCollections")
        public void testExtendJavaCollections() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxWithJava/builtinStubMethods/extendJavaCollections/");
            doTestWithJava(fileName);
        }

        @TestMetadata("substitutedIterable")
        public void testSubstitutedIterable() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxWithJava/builtinStubMethods/substitutedIterable/");
            doTestWithJava(fileName);
        }

        @TestMetadata("substitutedList")
        public void testSubstitutedList() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxWithJava/builtinStubMethods/substitutedList/");
            doTestWithJava(fileName);
        }

    }

    @TestMetadata("compiler/testData/codegen/boxWithJava/interfaces")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class Interfaces extends AbstractBlackBoxCodegenTest {
        @TestMetadata("abstractClassInheritsFromInterface")
        public void testAbstractClassInheritsFromInterface() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxWithJava/interfaces/abstractClassInheritsFromInterface/");
            doTestWithJava(fileName);
        }

        public void testAllFilesPresentInInterfaces() throws Exception {
            JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/codegen/boxWithJava/interfaces"), Pattern.compile("^([^\\.]+)$"), true);
        }

        @TestMetadata("inheritJavaInterface")
        public void testInheritJavaInterface() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxWithJava/interfaces/inheritJavaInterface/");
            doTestWithJava(fileName);
        }

    }

    @TestMetadata("compiler/testData/codegen/boxWithJava/jvmOverloads")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class JvmOverloads extends AbstractBlackBoxCodegenTest {
        public void testAllFilesPresentInJvmOverloads() throws Exception {
            JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/codegen/boxWithJava/jvmOverloads"), Pattern.compile("^([^\\.]+)$"), true);
        }

        @TestMetadata("generics")
        public void testGenerics() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxWithJava/jvmOverloads/generics/");
            doTestWithJava(fileName);
        }

        @TestMetadata("simple")
        public void testSimple() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxWithJava/jvmOverloads/simple/");
            doTestWithJava(fileName);
        }

    }

    @TestMetadata("compiler/testData/codegen/boxWithJava/notNullAssertions")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class NotNullAssertions extends AbstractBlackBoxCodegenTest {
        public void testAllFilesPresentInNotNullAssertions() throws Exception {
            JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/codegen/boxWithJava/notNullAssertions"), Pattern.compile("^([^\\.]+)$"), true);
        }

        @TestMetadata("extensionReceiverParameter")
        public void testExtensionReceiverParameter() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxWithJava/notNullAssertions/extensionReceiverParameter/");
            doTestWithJava(fileName);
        }

    }

    @TestMetadata("compiler/testData/codegen/boxWithJava/platformStatic")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class PlatformStatic extends AbstractBlackBoxCodegenTest {
        public void testAllFilesPresentInPlatformStatic() throws Exception {
            JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/codegen/boxWithJava/platformStatic"), Pattern.compile("^([^\\.]+)$"), true);
        }

        @TestMetadata("annotations")
        public void testAnnotations() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxWithJava/platformStatic/annotations/");
            doTestWithJava(fileName);
        }

        @TestMetadata("classObject")
        public void testClassObject() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxWithJava/platformStatic/classObject/");
            doTestWithJava(fileName);
        }

        @TestMetadata("enumCompanion")
        public void testEnumCompanion() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxWithJava/platformStatic/enumCompanion/");
            doTestWithJava(fileName);
        }

        @TestMetadata("object")
        public void testObject() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxWithJava/platformStatic/object/");
            doTestWithJava(fileName);
        }

    }

    @TestMetadata("compiler/testData/codegen/boxWithJava/properties")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class Properties extends AbstractBlackBoxCodegenTest {
        public void testAllFilesPresentInProperties() throws Exception {
            JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/codegen/boxWithJava/properties"), Pattern.compile("^([^\\.]+)$"), true);
        }

        @TestMetadata("annotationWithKotlinProperty")
        public void testAnnotationWithKotlinProperty() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxWithJava/properties/annotationWithKotlinProperty/");
            doTestWithJava(fileName);
        }

        @TestMetadata("classObjectProperties")
        public void testClassObjectProperties() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxWithJava/properties/classObjectProperties/");
            doTestWithJava(fileName);
        }

        @TestMetadata("substituteJavaSuperField")
        public void testSubstituteJavaSuperField() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxWithJava/properties/substituteJavaSuperField/");
            doTestWithJava(fileName);
        }

    }

    @TestMetadata("compiler/testData/codegen/boxWithJava/publicField")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class PublicField extends AbstractBlackBoxCodegenTest {
        public void testAllFilesPresentInPublicField() throws Exception {
            JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/codegen/boxWithJava/publicField"), Pattern.compile("^([^\\.]+)$"), true);
        }

        @TestMetadata("simple")
        public void testSimple() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxWithJava/publicField/simple/");
            doTestWithJava(fileName);
        }

    }

    @TestMetadata("compiler/testData/codegen/boxWithJava/reflection")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class Reflection extends AbstractBlackBoxCodegenTest {
        public void testAllFilesPresentInReflection() throws Exception {
            JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/codegen/boxWithJava/reflection"), Pattern.compile("^([^\\.]+)$"), true);
        }

        @TestMetadata("callInstanceJavaMethod")
        public void testCallInstanceJavaMethod() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxWithJava/reflection/callInstanceJavaMethod/");
            doTestWithJava(fileName);
        }

        @TestMetadata("callPrivateJavaMethod")
        public void testCallPrivateJavaMethod() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxWithJava/reflection/callPrivateJavaMethod/");
            doTestWithJava(fileName);
        }

        @TestMetadata("callStaticJavaMethod")
        public void testCallStaticJavaMethod() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxWithJava/reflection/callStaticJavaMethod/");
            doTestWithJava(fileName);
        }

        @TestMetadata("functionReferenceErasedToKFunction")
        public void testFunctionReferenceErasedToKFunction() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxWithJava/reflection/functionReferenceErasedToKFunction/");
            doTestWithJava(fileName);
        }

        @TestMetadata("javaClassGetFunctions")
        public void testJavaClassGetFunctions() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxWithJava/reflection/javaClassGetFunctions/");
            doTestWithJava(fileName);
        }

        @TestMetadata("javaMethodsSmokeTest")
        public void testJavaMethodsSmokeTest() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxWithJava/reflection/javaMethodsSmokeTest/");
            doTestWithJava(fileName);
        }

        @TestMetadata("javaPropertyInheritedInKotlin")
        public void testJavaPropertyInheritedInKotlin() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxWithJava/reflection/javaPropertyInheritedInKotlin/");
            doTestWithJava(fileName);
        }

        @TestMetadata("javaStaticField")
        public void testJavaStaticField() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxWithJava/reflection/javaStaticField/");
            doTestWithJava(fileName);
        }

        @TestMetadata("kotlinPropertyInheritedInJava")
        public void testKotlinPropertyInheritedInJava() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxWithJava/reflection/kotlinPropertyInheritedInJava/");
            doTestWithJava(fileName);
        }

        @TestMetadata("mutatePrivateJavaInstanceField")
        public void testMutatePrivateJavaInstanceField() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxWithJava/reflection/mutatePrivateJavaInstanceField/");
            doTestWithJava(fileName);
        }

        @TestMetadata("mutatePrivateJavaStaticField")
        public void testMutatePrivateJavaStaticField() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxWithJava/reflection/mutatePrivateJavaStaticField/");
            doTestWithJava(fileName);
        }

        @TestMetadata("noConflictOnKotlinGetterAndJavaField")
        public void testNoConflictOnKotlinGetterAndJavaField() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxWithJava/reflection/noConflictOnKotlinGetterAndJavaField/");
            doTestWithJava(fileName);
        }

        @TestMetadata("parametersHaveNoNames")
        public void testParametersHaveNoNames() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxWithJava/reflection/parametersHaveNoNames/");
            doTestWithJava(fileName);
        }

        @TestMetadata("referenceToJavaFieldOfKotlinSubclass")
        public void testReferenceToJavaFieldOfKotlinSubclass() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxWithJava/reflection/referenceToJavaFieldOfKotlinSubclass/");
            doTestWithJava(fileName);
        }

    }

    @TestMetadata("compiler/testData/codegen/boxWithJava/secondaryConstructors")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class SecondaryConstructors extends AbstractBlackBoxCodegenTest {
        public void testAllFilesPresentInSecondaryConstructors() throws Exception {
            JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/codegen/boxWithJava/secondaryConstructors"), Pattern.compile("^([^\\.]+)$"), true);
        }

        @TestMetadata("withGenerics")
        public void testWithGenerics() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxWithJava/secondaryConstructors/withGenerics/");
            doTestWithJava(fileName);
        }

        @TestMetadata("withPrimary")
        public void testWithPrimary() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxWithJava/secondaryConstructors/withPrimary/");
            doTestWithJava(fileName);
        }

        @TestMetadata("withVarargs")
        public void testWithVarargs() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxWithJava/secondaryConstructors/withVarargs/");
            doTestWithJava(fileName);
        }

        @TestMetadata("withoutPrimary")
        public void testWithoutPrimary() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxWithJava/secondaryConstructors/withoutPrimary/");
            doTestWithJava(fileName);
        }

    }
}
