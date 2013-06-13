/*
 * Copyright 2013-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package efx.data.model;

import javafx.beans.property.BooleanProperty
import javafx.beans.property.DoubleProperty
import javafx.beans.property.FloatProperty
import javafx.beans.property.IntegerProperty
import javafx.beans.property.ListProperty
import javafx.beans.property.LongProperty
import javafx.beans.property.MapProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SetProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleFloatProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleMapProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleSetProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.collections.ObservableMap
import javafx.collections.ObservableSet

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.AnnotatedNode
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.FieldNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.Parameter
import org.codehaus.groovy.ast.PropertyNode
import org.codehaus.groovy.ast.builder.AstBuilder
import org.codehaus.groovy.ast.expr.ArgumentListExpression
import org.codehaus.groovy.ast.expr.BinaryExpression
import org.codehaus.groovy.ast.expr.BooleanExpression
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.ConstructorCallExpression
import org.codehaus.groovy.ast.expr.Expression
import org.codehaus.groovy.ast.expr.FieldExpression
import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codehaus.groovy.ast.expr.StaticMethodCallExpression
import org.codehaus.groovy.ast.expr.VariableExpression
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.stmt.EmptyStatement
import org.codehaus.groovy.ast.stmt.ExpressionStatement
import org.codehaus.groovy.ast.stmt.IfStatement
import org.codehaus.groovy.ast.stmt.ReturnStatement
import org.codehaus.groovy.ast.stmt.Statement
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.control.messages.SyntaxErrorMessage
import org.codehaus.groovy.runtime.MetaClassHelper
import org.codehaus.groovy.syntax.SyntaxException
import org.codehaus.groovy.syntax.Token
import org.codehaus.groovy.syntax.Types
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation
import org.objectweb.asm.Opcodes

import efx.data.model.annotation.FXBindable
import efx.data.model.annotation.Field

/**
 * 
 * @author Franklyn Donald <lovefree103@gmail.com>
 *
 */
@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)  
public class FXBindableASTTransformation implements ASTTransformation, Opcodes {

	protected static final ClassNode boundClassNode = ClassHelper.make(FXBindable.class);
	protected static final ClassNode fieldClassNode = ClassHelper.make(Field.class);

	protected static final ClassNode objectPropertyClass = ClassHelper.make(ObjectProperty.class, true);
	protected static final ClassNode booleanPropertyClass = ClassHelper.make(BooleanProperty.class);
	protected static final ClassNode doublePropertyClass = ClassHelper.make(DoubleProperty.class);
	protected static final ClassNode floatPropertyClass = ClassHelper.make(FloatProperty.class);
	protected static final ClassNode intPropertyClass = ClassHelper.make(IntegerProperty.class);
	protected static final ClassNode longPropertyClass = ClassHelper.make(LongProperty.class);
	protected static final ClassNode stringPropertyClass = ClassHelper.make(StringProperty.class);
	protected static final ClassNode listPropertyClass = ClassHelper.make(ListProperty.class);
	protected static final ClassNode mapPropertyClass = ClassHelper.make(MapProperty.class);
	protected static final ClassNode setPropertyClass = ClassHelper.make(SetProperty.class);

	protected static final ClassNode simpleBooleanPropertyClass = ClassHelper.make(SimpleBooleanProperty.class);
	protected static final ClassNode simpleDoublePropertyClass = ClassHelper.make(SimpleDoubleProperty.class);
	protected static final ClassNode simpleFloatPropertyClass = ClassHelper.make(SimpleFloatProperty.class);
	protected static final ClassNode simpleIntPropertyClass = ClassHelper.make(SimpleIntegerProperty.class);
	protected static final ClassNode simpleLongPropertyClass = ClassHelper.make(SimpleLongProperty.class);
	protected static final ClassNode simpleStringPropertyClass = ClassHelper.make(SimpleStringProperty.class);
	protected static final ClassNode simpleListPropertyClass = ClassHelper.make(SimpleListProperty.class);
	protected static final ClassNode simpleMapPropertyClass = ClassHelper.make(SimpleMapProperty.class);
	protected static final ClassNode simpleSetPropertyClass = ClassHelper.make(SimpleSetProperty.class);
	protected static final ClassNode simpleObjectPropertyClass = ClassHelper.make(SimpleObjectProperty.class, true);

	protected static final ClassNode observableListClass = ClassHelper.make(ObservableList.class, true);
	protected static final ClassNode observableMapClass = ClassHelper.make(ObservableMap.class, true);
	protected static final ClassNode observableSetClass = ClassHelper.make(ObservableSet.class, true);
	protected static final ClassNode fxCollectionsType = ClassHelper.make(FXCollections.class, true);

	protected static final ClassNode listType = ClassHelper.make(List.class, true);
	protected static final ClassNode mapType = ClassHelper.make(Map.class, true);
	protected static final ClassNode setType = ClassHelper.make(Set.class, true);
  
	private static final Map<ClassNode, ClassNode> propertyTypeMap = new HashMap<ClassNode, ClassNode>();

	private static final Map<ClassNode, ClassNode> propertyImplMap = new HashMap<ClassNode, ClassNode>();

	static {
		propertyTypeMap.putAll (			
			(ClassHelper.STRING_TYPE) 	: stringPropertyClass,
			(ClassHelper.boolean_TYPE) 	: booleanPropertyClass,
			(ClassHelper.Boolean_TYPE)	: booleanPropertyClass,
			(ClassHelper.double_TYPE)	: doublePropertyClass,
			(ClassHelper.Double_TYPE)	: doublePropertyClass,
			(ClassHelper.float_TYPE)	: floatPropertyClass,
			(ClassHelper.Float_TYPE)	: floatPropertyClass,
			(ClassHelper.int_TYPE)		: intPropertyClass,
			(ClassHelper.Integer_TYPE)	: intPropertyClass,
			(ClassHelper.long_TYPE)		: longPropertyClass,
			(ClassHelper.Long_TYPE)		: longPropertyClass,
			(ClassHelper.short_TYPE)	: intPropertyClass,
			(ClassHelper.Short_TYPE)	: intPropertyClass,
			(ClassHelper.byte_TYPE)		: intPropertyClass,
			(ClassHelper.Byte_TYPE)		: intPropertyClass,
			(listType)					: listPropertyClass, 
			(mapType)					: mapPropertyClass,
			(setType)					: setPropertyClass
		)

		propertyImplMap.putAll (
			(booleanPropertyClass)		: simpleBooleanPropertyClass,
			(doublePropertyClass)		: simpleDoublePropertyClass,
			(floatPropertyClass)		: simpleFloatPropertyClass,
			(intPropertyClass)			: simpleIntPropertyClass,
			(longPropertyClass)			: simpleLongPropertyClass,
			(stringPropertyClass)		: simpleStringPropertyClass,
			(listPropertyClass)			: simpleListPropertyClass,
			(mapPropertyClass)			: simpleMapPropertyClass,
			(setPropertyClass)			: simpleSetPropertyClass,
			(objectPropertyClass)		: simpleObjectPropertyClass
		)
	}

	/**
	 * Convenience method to see if an annotated node is {@code @FXBindable}.
	 *
	 * @param node the node to check
	 * @return true if the node is FXBindable
	 */
	public static boolean hasFXBindableAnnotation(AnnotatedNode node) {
		node.getAnnotations().find {
			it.classNode == boundClassNode
		}
	}
	
	@Override
	public void visit(ASTNode[] nodes, SourceUnit sourceUnit) {
		if (!(nodes[0] instanceof AnnotationNode) || !(nodes[1] instanceof AnnotatedNode)) {
			throw new RuntimeException("Internal error: wrong types: node.class / parent.class");
		}
		AnnotationNode node = (AnnotationNode) nodes[0];
		AnnotatedNode parent = (AnnotatedNode) nodes[1];

		if (parent instanceof FieldNode) {
			int modifiers = ((FieldNode) parent).getModifiers();
			if ((modifiers & Opcodes.ACC_FINAL) != 0) {
				String msg = "@efx.data.model.annotation.FXBindable cannot annotate a final property.";
				generateSyntaxErrorMessage(sourceUnit, node, msg);
			}
			addJavaFXProperty(sourceUnit, node, parent.declaringClass, (FieldNode) parent);
		} else if(parent instanceof ClassNode){
			addJavaFXPropertyToClass(sourceUnit, node, (ClassNode) parent);
		}
	}

	private boolean filter(FieldNode field){
		if (hasFXBindableAnnotation(field)
		|| ((field.getModifiers() & Opcodes.ACC_FINAL) != 0)
		|| field.isStatic() || isCollectionType(field)) {
			// explicitly labeled properties are already handled,
			// don't transform final properties
			// don't transform static properties
			// VetoableASTTransformation will handle both @Bindable and @Vetoable
			return true;
		}

		return false;
	}

	private boolean isCollectionType(FieldNode field){
		ClassNode clazz = field.getType();
		if(clazz.equals(observableListClass) || clazz.equals(observableMapClass) || clazz.equals(observableSetClass) || clazz.equals(fxCollectionsType)){
			return true;
		}
		return false;
	}

	/**
	 * Adds a JavaFX property to the class in place of the original Groovy property.  A pair of synthetic
	 * getter/setter methods is generated to provide pseudo-access to the original property.
	 *
	 * @param source The SourceUnit in which the annotation was found
	 * @param node The node that was annotated
	 * @param declaringClass The class in which the annotation was found
	 * @param field The field upon which the annotation was placed
	 */
	private void addJavaFXProperty(SourceUnit source, AnnotationNode node, ClassNode declaringClass, FieldNode field) {
		String fieldName = field.getName();
		for (PropertyNode propertyNode : declaringClass.getProperties()) {
			if (propertyNode.getName().equals(fieldName)) {
				if ((!field.isStatic()) && (!isCollectionType(field))) {
					createPropertyGetterSetter(declaringClass, propertyNode);
				}
				return;
			}
		}

		//noinspection ThrowableInstanceNeverThrown
		String message = "@efx.data.model.annotation.FXBindable must be on a property, not a field.  Try removing the private, protected, or public modifier."
		generateSyntaxErrorMessage(source, node, message);
	}

	/**
	 * Iterate through the properties of the class and convert each eligible property to a JavaFX property.
	 * 
	 * @param source The SourceUnit
	 * @param node The AnnotationNode
	 * @param classNode The declaring class
	 */
	private void addJavaFXPropertyToClass(SourceUnit source, AnnotationNode node, ClassNode classNode) {
		for (PropertyNode propertyNode : classNode.getProperties()) {
			FieldNode field = propertyNode.getField();
			// look to see if per-field handlers will catch this one...			
			if(!filter(field)){
				createPropertyGetterSetter(classNode, propertyNode);
			}
			
		}
	}

	/**
	 * Creates the JavaFX property and three methods for accessing the property and a pair of
	 * getter/setter methods for accessing the original (now synthetic) Groovy property.  For
	 * example, if the original property was "String firstName" then these three methods would
	 * be generated:
	 *
	 *     public String getFirstName()
	 *     public void setFirstName(String value)
	 *     public StringProperty firstNameProperty()
	 *
	 * @param classNode The declaring class in which the property will appear
	 * @param originalProp The original Groovy property
	 */
	private void createPropertyGetterSetter(ClassNode classNode, PropertyNode originalProp) {
		Expression initExp = originalProp.getInitialExpression();
		originalProp.getField().setInitialValueExpression(null);

		PropertyNode fxProperty = createFXProperty(originalProp);

		// create groovy bean setter method
		String setterName = "set" + MetaClassHelper.capitalize(originalProp.getName());
		if (classNode.getMethods(setterName).isEmpty()) {
			Statement setterBlock = createSetterStatement(fxProperty);
			createSetterMethod(classNode, originalProp, setterName, setterBlock);
		}

		// create groovy bean getter method
		String getterName = "get" + MetaClassHelper.capitalize(originalProp.getName());
		if (classNode.getMethods(getterName).isEmpty()) {
			Statement getterBlock = createGetterStatement(fxProperty);
			createGetterMethod(classNode, originalProp, getterName, getterBlock);
		}
		
		FieldNode fxFieldShortName = createFieldNodeCopy(originalProp.getName(), null, fxProperty.getField());
		createPropertyAccessor(classNode, createFXProperty(originalProp), fxFieldShortName, initExp);

		classNode.removeField(originalProp.getName());
		classNode.addField(fxFieldShortName);
	}

	/**
	 * Creates a new PropertyNode for the JavaFX property based on the original property.  The new property
	 * will have "Property" appended to its name and its type will be one of the *Property types in JavaFX.
	 *
	 * @param orig The original property
	 * @return A new PropertyNode for the JavaFX property
	 */
	private PropertyNode createFXProperty(PropertyNode orig) {
		ClassNode origType = orig.getType();
		ClassNode newType = propertyTypeMap.get(origType);

		if(newType == null){
			newType = new ClassNode(ObjectProperty.class);
		}
		
		FieldNode fieldNode = createFieldNodeCopy(orig.getName() + "Property", newType, orig.getField());
		return new PropertyNode(fieldNode, orig.getModifiers(), orig.getGetterBlock(), orig.getSetterBlock());
	}

	/**
	 * Creates a setter method and adds it to the declaring class.  The setter has the form:
	 *
	 *     void <setter>(<type> fieldName)
	 *
	 * @param declaringClass The class to which the method is added
	 * @param propertyNode The property node being accessed by this setter
	 * @param setterName The name of the setter method
	 * @param setterBlock The code body of the method
	 */
	protected void createSetterMethod(ClassNode declaringClass, PropertyNode propertyNode, String setterName,
			Statement setterBlock) {
		Parameter[] setterParameterTypes = [new Parameter(propertyNode.getType(), "value")];
		int mod = propertyNode.getModifiers() | Opcodes.ACC_FINAL;

		MethodNode setter = new MethodNode(setterName, mod, ClassHelper.VOID_TYPE, setterParameterTypes,
				ClassNode.EMPTY_ARRAY, setterBlock);
		setter.setSynthetic(true);
		declaringClass.addMethod(setter); 
	}

	/**
	 * Creates a getter method and adds it to the declaring class.
	 *
	 * @param declaringClass The class to which the method is added
	 * @param propertyNode The property node being accessed by this getter
	 * @param getterName The name of the getter method
	 * @param getterBlock The code body of the method
	 */
	protected void createGetterMethod(ClassNode declaringClass, PropertyNode propertyNode, String getterName,
			Statement getterBlock) {
		int mod = propertyNode.getModifiers() | Opcodes.ACC_FINAL;
		MethodNode getter = new MethodNode(getterName, mod, propertyNode.getType(), Parameter.EMPTY_ARRAY,
				ClassNode.EMPTY_ARRAY, getterBlock);
		getter.setSynthetic(true);
		declaringClass.addMethod(getter);
	}


	/**
	 * Creates the body of a property access method that returns the JavaFX *Property instance.  If
	 * the original property was "String firstName" then the generated code would be:
	 *
	 *     if (firstNameProperty == null) {
	 *         firstNameProperty = new javafx.beans.property.SimpleStringProperty()
	 *     }
	 *     return firstNameProperty
	 *
	 * @param classNode The declaring class to which the JavaFX property will be added
	 * @param fxProperty The new JavaFX property
	 * @param fxFieldShortName
	 * @param initExp The initializer expression from the original Groovy property declaration
	 */
	private void createPropertyAccessor(ClassNode classNode, PropertyNode fxProperty, FieldNode fxFieldShortName,
			Expression initExp) {
		FieldExpression fieldExpression = new FieldExpression(fxFieldShortName);

		ArgumentListExpression ctorArgs = initExp == null ?
				ArgumentListExpression.EMPTY_ARGUMENTS :
				new ArgumentListExpression(initExp);

		BlockStatement block = new BlockStatement();
		ClassNode fxType = fxProperty.getType();
		ClassNode implNode = propertyImplMap.get(fxType);		
		Expression initExpression = new ConstructorCallExpression(implNode, ctorArgs);

		IfStatement ifStmt = new IfStatement(
				new BooleanExpression(
				new BinaryExpression(
				fieldExpression,
				Token.newSymbol(Types.COMPARE_EQUAL, 0, 0),
				ConstantExpression.NULL
				)
				),
				new ExpressionStatement(
				new BinaryExpression(
				fieldExpression,
				Token.newSymbol(Types.EQUAL, 0, 0),
				initExpression
				)
				),
				EmptyStatement.INSTANCE
				);
		block.addStatement(ifStmt);
		block.addStatement(new ReturnStatement(fieldExpression));

		String getterName = getFXPropertyGetterName(fxProperty);
		MethodNode accessor = new MethodNode(getterName, fxProperty.getModifiers(), fxProperty.getType(),
				Parameter.EMPTY_ARRAY, ClassNode.EMPTY_ARRAY, block);
		accessor.setSynthetic(true);
		classNode.addMethod(accessor);

		// Create the xxxxProperty() method that merely calls getXxxxProperty()
		block = new BlockStatement();

		VariableExpression thisExpression = VariableExpression.THIS_EXPRESSION;
		ArgumentListExpression emptyArguments = ArgumentListExpression.EMPTY_ARGUMENTS;

		MethodCallExpression getProperty = new MethodCallExpression(thisExpression, getterName, emptyArguments);
		block.addStatement(new ReturnStatement(getProperty));

		String javaFXPropertyFunction = fxProperty.getName();

		accessor = new MethodNode(javaFXPropertyFunction, fxProperty.getModifiers(), fxProperty.getType(),
				Parameter.EMPTY_ARRAY, ClassNode.EMPTY_ARRAY, block);
		accessor.setSynthetic(true);
		classNode.addMethod(accessor);

		// Create the xxxx() method that merely calls getXxxxProperty()
		block = new BlockStatement();

		thisExpression = VariableExpression.THIS_EXPRESSION;
		emptyArguments = ArgumentListExpression.EMPTY_ARGUMENTS;

		getProperty = new MethodCallExpression(thisExpression, getterName, emptyArguments);
		block.addStatement(new ReturnStatement(getProperty));
		javaFXPropertyFunction = fxProperty.getName().replace("Property", "");

		accessor = new MethodNode(javaFXPropertyFunction, fxProperty.getModifiers(), fxProperty.getType(),
				Parameter.EMPTY_ARRAY, ClassNode.EMPTY_ARRAY, block);
		accessor.setSynthetic(true);
		classNode.addMethod(accessor);


	}

	/**
	 * Creates the body of a setter method for the original property that is actually backed by a
	 * JavaFX *Property instance:
	 *
	 *     Object $property = this.someProperty()
	 *     $property.setValue(value)
	 *
	 * @param fxProperty The original Groovy property that we're creating a setter for.
	 * @return A Statement that is the body of the new setter.
	 */
	protected Statement createSetterStatement(PropertyNode fxProperty) {
		String fxPropertyGetter = getFXPropertyGetterName(fxProperty);

		MethodCallExpression getProperty = new MethodCallExpression(VariableExpression.THIS_EXPRESSION, fxPropertyGetter, ArgumentListExpression.EMPTY_ARGUMENTS);

		VariableExpression setVariable = new VariableExpression("value");
		ClassNode type = fxProperty.getType();
		MethodCallExpression setValue = null;
		if(type.equals(setPropertyClass)){
			StaticMethodCallExpression castToObservableSet = new StaticMethodCallExpression(fxCollectionsType, "observableSet", setVariable);
			setValue = new MethodCallExpression(getProperty, "set", castToObservableSet);
		}else if(type.equals(listPropertyClass)){
			StaticMethodCallExpression castToObservableList = new StaticMethodCallExpression(fxCollectionsType, "observableList", setVariable);
			setValue = new MethodCallExpression(getProperty, "set", castToObservableList);
		}else if(type.equals(mapPropertyClass)){
			StaticMethodCallExpression castToObservableMap = new StaticMethodCallExpression(fxCollectionsType, "observableMap", setVariable);
			setValue = new MethodCallExpression(getProperty, "set", castToObservableMap);
		}else{
			ArgumentListExpression valueArg = new ArgumentListExpression(setVariable);
			setValue = new MethodCallExpression(getProperty, "set", valueArg);
		}

		return new ExpressionStatement(setValue);
	}


	/**
	 * Creates the body of a getter method for the original property that is actually backed by a
	 * JavaFX *Property instance:
	 *
	 *     Object $property = this.someProperty()
	 *     return $property.getValue()
	 *
	 * @param fxProperty The new JavaFX property.
	 * @return A Statement that is the body of the new getter.
	 */
	protected Statement createGetterStatement(PropertyNode fxProperty) {
		String fxPropertyGetter = getFXPropertyGetterName(fxProperty);
		
		VariableExpression thisExpression = VariableExpression.THIS_EXPRESSION;
		ArgumentListExpression emptyArguments = ArgumentListExpression.EMPTY_ARGUMENTS;

		MethodCallExpression getProperty = new MethodCallExpression(thisExpression, fxPropertyGetter, emptyArguments);
		MethodCallExpression getValue = new MethodCallExpression(getProperty, "get", emptyArguments);

		return new ReturnStatement(new ExpressionStatement(getValue));
	}

	/**
	 * Generates a SyntaxErrorMessage based on the current SourceUnit, AnnotationNode, and a specified
	 * error message.
	 *
	 * @param sourceUnit The SourceUnit
	 * @param node The node that was annotated
	 * @param msg The error message to display
	 */
	private void generateSyntaxErrorMessage(SourceUnit sourceUnit, AnnotationNode node, String msg) {
		SyntaxException error = new SyntaxException(msg, node.getLineNumber(), node.getColumnNumber());
		sourceUnit.getErrorCollector().addErrorAndContinue(new SyntaxErrorMessage(error, sourceUnit));
	}

	/**
	 * Creates a copy of a FieldNode with a new name and, optionally, a new type.
	 *
	 * @param newName The name for the new field node.
	 * @param newType The new type of the field.  If null, the old FieldNode's type will be used.
	 * @param f The FieldNode to copy.
	 * @return The new FieldNode.
	 */
	private FieldNode createFieldNodeCopy(String newName, ClassNode newType, FieldNode f) {
		if (newType == null)
			newType = f.getType();
		newType = newType.getPlainNodeReference();
		FieldNode nf = new FieldNode(newName, f.getModifiers(), newType, f.getOwner(), f.getInitialValueExpression());

		List<AnnotationNode> ans = f.getAnnotations();
		boolean containFieldAnno = false;
		for(AnnotationNode an:ans){
			Class<?> clazz = an.getClassNode().getTypeClass();
			if(clazz == boundClassNode.getTypeClass()){
				continue;
			}else if(clazz == fieldClassNode.getTypeClass()){
				containFieldAnno = true;
			}
			nf.addAnnotation(an);
		}

		if(!containFieldAnno){
			nf.addAnnotation(new AnnotationNode(fieldClassNode));
		}

		return nf;
	}

	/**
	 * Generates the correct getter method name for a JavaFX property.
	 * 
	 * @param fxProperty The property for which the getter should be generated.
	 * @return The getter name as a String.
	 */
	private String getFXPropertyGetterName(PropertyNode fxProperty) {
		//    	if(fxProperty.getType().getTypeClass() == booleanPropertyClass.getTypeClass()){
		//    		return "is" + capitalize(fxProperty.getName());
		//    	}
		return "get" + capitalize(fxProperty.getName());
	}

	/**
	 * Capitalize the first letter of the given string.
	 *
	 * @param string The source string
	 * @return The capitalized string
	 */
	private String capitalize(String string) {
		if (string == null || string.equals(""))
			return string;
		return "${string[0].toUpperCase()}${string[1..-1]}"	
	}
}

