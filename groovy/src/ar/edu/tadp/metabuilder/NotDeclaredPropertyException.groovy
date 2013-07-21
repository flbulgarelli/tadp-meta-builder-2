package ar.edu.tadp.metabuilder

class NotDeclaredPropertyException extends RuntimeException {
	
	NotDeclaredPropertyException(name){
		super("The property $name was not declared in the builder before it was setted")
	}
	
}
