package ar.edu.tadp.metabuilder

class InvalidValuesException extends RuntimeException {
	
	InvalidValuesException(values) {
		super("The values $values provided don't satisfy the validations of the builder")
	}
}
