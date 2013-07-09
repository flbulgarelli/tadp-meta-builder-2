package ar.edu.tadp.metabuilder

class ConditionalBehaviour {

	def name
	def condition
	def behaviour

	ConditionalBehaviour(name, condition, behaviour) {
		this.behaviour = behaviour
		this.condition = condition
		this.name = name
	}
	
	def addBehaviour(anObject) {
		if(anObject.with(condition)) {
			anObject.metaClass."$name" = behaviour
		}
	}
}
