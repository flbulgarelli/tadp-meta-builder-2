package ar.edu.tadp.metabuilder

import org.junit.Before
import org.junit.Test

class MetaBuilderTest {

	@Test
	void testPunto1_1() {
		def metaBuilder = builderPunto1()
		def perroBuilder = metaBuilder.build();

		perroBuilder.edad = 4
		perroBuilder.peso = 14
		perroBuilder.raza = 'fox terrier'

		checkPerroConstruido(perroBuilder.build())
	}

	private MetaBuilder builderPunto1() {
		def metaBuilder = new MetaBuilder()
		metaBuilder.addProperty('raza')
		metaBuilder.addProperty('edad')
		metaBuilder.addProperty('peso')

		metaBuilder.setTargetClass(Perro)
		return metaBuilder
	}
	
	@Test(expected=NotDeclaredPropertyException)
	void testPunto1_2() {
		def perroBuilder = builderPunto1().build();
		perroBuilder.duenio = 'magoya'
	}

	def checkPerroConstruido(unPerro) {
		assert unPerro.edad == 4
		assert unPerro.peso == 14
		assert unPerro.raza == 'fox terrier'
	}

	@Test
	void testPunto2() {
		def builderDePerros = MetaBuilder.build {
			property('raza')
			property('edad')
			property('peso')
			targetClass(Perro)
		}
		
		checkPerroConstruido(buildDefault(builderDePerros))
	}
	
	def buildDefault(aBuilder) {
		aBuilder.build {
			edad = 4
			peso = 14
			raza = 'fox terrier'
		}
	}
	
	def builderPunto3() {
		MetaBuilder.build {
			property('raza')
			property('edad')
			property('peso')
			targetClass(Perro)
			validate {
				raza in ['fox terrier', 'salchicha', "chiuaua"]
			}
			validate {
				edad > 0 && edad < 20
			}
		}
	}
	
	@Test
	void testPunto3_1() {
		def builderDePerros = builderPunto3()
		checkPerroConstruido(buildDefault(builderDePerros))
	}
	
	@Test(expected=InvalidValuesException)
	void testPunto3_2() {
		def builderDePerros = builderPunto3()
		builderDePerros.build {
			edad = 5
			peso = 10
			raza = 'calle'
		}
	}
	
	@Test
	void testPunto4() {
		def builderDePerros = MetaBuilder.build {
			property('raza')
			property('edad')
			property('peso')
			targetClass(Perro)
			validate {
				raza in ['fox terrier', 'salchicha', "chiuaua"]
			}
			validate {
				edad > 0 && edad < 20
			}
			behaveWhen ('expectativaVida', { raza == 'fox terrier' } ) {
				20 - edad
			}
			behaveWhen ('expectativaVida', { raza == 'salchicha' } ) {
				edad + peso * 2
			}
			behaveWhen ('expectativaVida', { raza == 'chiuaua' } ) {
				pacienciaDuenio() / 1000
			}
		}
		
		def unFoxTerrier = buildDefault(builderDePerros)
		assert unFoxTerrier.expectativaVida() == 16
		
		def unSalchicha = builderDePerros.build {
			edad = 2
			peso = 9
			raza = 'salchicha'
		}
		assert unSalchicha.expectativaVida() == 20
	}
}
