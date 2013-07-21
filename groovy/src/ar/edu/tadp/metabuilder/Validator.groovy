package ar.edu.tadp.metabuilder

class Validator {
	
	def closure
	
	def builder

	Validator(aClosure) {
		this.closure = aClosure
	}
	
	def validate(aBuilder) {
		this.builder = aBuilder
		if(!this.with(closure)) {
			throw new InvalidValuesException(aBuilder.values)
		}
	}
	
	def propertyMissing(String name) {
		return builder.getValue(name)
	}	
}
