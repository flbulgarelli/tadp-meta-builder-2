package ar.edu.tadp.metabuilder

class GenericBuilder {

	def targetProperties = []
	def values = [:]
	def targetClass

	def validations = []
	def behaviours = []

	def getValue(name) {
		assertDeclaredProperty(name)
		return values[name]
	}
	
	def addConditionalBehaviour(methodName, condition, behaviour) {
		behaviours.add(new ConditionalBehaviour(methodName, condition, behaviour))
	}

	def assertDeclaredProperty(name) {
		if(!targetProperties.contains(name)) {
			throw new NotDeclaredPropertyException(name)
		}
	}

	def addProperty(aProperty) {
		targetProperties.add(aProperty)
	}

	def addValidation(aClosure) {
		validations.add(new Validator(aClosure))
	}

	def setTargetClass(aClass) {
		targetClass = aClass
	}

	def propertyMissing(String name, value) {
		assertDeclaredProperty(name)
		values[name] = value
	}
	
	def validate() {
		validations.each {
			it.validate(this)
		}
	}

	def build() {
		//Decisión de diseño. Quiero que estalle sólo si se ejecuta el build. Los valores podrían ser transitorios.
		validate()
		def instance = targetClass.newInstance()

		values.each { key, value ->
			instance."$key" = value
		}
		
		behaviours.each {
			it.addBehaviour(instance)
		}

		return instance
	}

	def build(aClosure) {
		this.clear();
		this.with(aClosure)

		return this.build()
	}

	def clear() {
		this.values.clear();
	}
}
