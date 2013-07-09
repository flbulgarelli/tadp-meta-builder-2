package ar.edu.tadp.metabuilder;

class MetaBuilder {
	
	def builder = new GenericBuilder()
	
	def addProperty(aProperty) {
		builder.addProperty(aProperty)
	}
	
	def setTargetClass(aClass) { 
		builder.setTargetClass(aClass)
	}
	
	def build() {
		builder
	}
	
	def property(aProperty) {
		addProperty(aProperty)
	}
	
	def targetClass(aClass) {
		setTargetClass(aClass)
	}
	
	def validate(aClosure) {
		builder.addValidation(aClosure)
	}
	
	def behaveWhen(methodName, condition, behaviour) {
		builder.addConditionalBehaviour(methodName, condition, behaviour)
	}
	
	static build(aClosure) {
		def metaBuilder = new MetaBuilder()
		
		metaBuilder.with(aClosure)
		
		return metaBuilder.build()
	}
}
