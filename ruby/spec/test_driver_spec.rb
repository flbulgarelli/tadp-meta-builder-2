require 'rspec'

class Perro
  attr_accessor :raza, :edad, :peso, :duenio
end

module MetaBuilderDSL
  def property(name)
    add_property(name)
  end

  def target_class(clazz)
    self.target_class = clazz
  end
end

class MetaBuilder
  include MetaBuilderDSL

  def initialize
    @builder = GenericBuilder.new
  end

  def target_class=(clazz)
    @builder.target_class=clazz
  end

  def add_property(name)
    @builder.add_property(name)
  end

  def build
    @builder
  end

  def self.build(&block)
    meta_builder = MetaBuilder.new
    meta_builder.instance_eval(&block)
    meta_builder.build
  end
end

class GenericBuilder
  def target_class=(clazz)
    @built = clazz.new
  end

  def add_property(name)
    define_singleton_method("#{name}=") do |value|
      @built.send "#{name}=", value
    end
  end

  def build(&block)
    instance_eval(&block) if (block_given?)
    @built
  end
end

describe 'meta builder' do

  def builder_ejemplo
    meta_builder = MetaBuilder.new
    meta_builder.add_property :raza
    meta_builder.add_property :edad
    meta_builder.add_property :peso

    meta_builder.target_class = Perro
    meta_builder
  end

  def validar_perro(perro)
    perro.edad.should == 4
    perro.peso.should == 14
    perro.raza.should == 'fox terrier'
  end


  context 'al utilizar la sintaxis basica' do
    it 'puede construir builders basicos' do
      meta_builder = builder_ejemplo
      perro_builder = meta_builder.build

      perro_builder.edad = 4
      perro_builder.peso = 14
      perro_builder.raza = 'fox terrier'

      perro = perro_builder.build
      validar_perro(perro)
    end

    it 'construye builders que solo pueden setear las propiedades indicadas' do
      expect {
        perro_builder = builder_ejemplo.build
        perro_builder.duenio = 'magoya'
      }.to raise_error
    end
  end

  context 'al utilizar la sintaxis anidada' do
    it 'se comporta igual que con la sintaxis basica' do
      perro_builder = MetaBuilder.build do
        property :raza
        property :edad
        property :peso
        target_class Perro
      end
      perro = perro_builder.build do
        self.edad = 4
        self.peso = 14
        self.raza = 'fox terrier'
      end
      validar_perro(perro)
    end
  end

  context 'al utilizar validaciones' do
    it 'construye si las mismas se cumplen' do
      pending
    end

    it 'falla si las mismas no se cumplen' do
      perro_builder = MetaBuilder.build do
        property :raza
        property :edad
        property :peso
        target_class Perro
        validate do
          ['fox terrier', 'salchicha', 'chiuaua'].include? raza
        end
        validate do
          edad > 0 && edad < 20
        end
      end
      perro_builder.build do
        self.edad = 5
        self.peso = 10
        self.raza = 'calle'
      end
      pending
    end
  end


  context 'al utilizar agregado de comportamiento selectivo' do
    it 'agrega el comportamiento solo si cumple la condicion' do
      perro_builder = MetaBuilder.build do
        property :raza
        property :edad
        property :peso
        target_class Perro
        validate do
          ['fox terrier', 'salchicha', 'chiuaua'].include? raza
        end
        validate do
          edad > 0 && edad < 20
        end
        #behaveWhen('expectativaVida', lambda { raza == 'fox terrier' }) do
        #  20 - edad
        #end
        #behaveWhen ('expectativaVida', lambda { raza == 'salchicha' }) do
        #  edad + peso * 2
        #end
        #behaveWhen ('expectativaVida', lambda { raza == 'chiuaua' }) do
        #  pacienciaDuenio / 1000
        #end
      end
      pending
    end
  end


end


#@Test
#void testPunto4() {
#  def builderDePerros
#    = MetaBuilder.build {
#      property('raza')
#      property('edad')
#      property('peso')
#      targetClass(Perro)
#      validate {
#        raza in ['fox terrier', 'salchicha', "chiuaua"]
#      }
#      validate {
#        edad > 0 && edad < 20
#      }
#      behaveWhen ('expectativaVida', {raza == 'fox terrier'}) {
#        20 - edad
#      }
#      behaveWhen ('expectativaVida', {raza == 'salchicha'}) {
#        edad + peso * 2
#      }
#      behaveWhen ('expectativaVida', {raza == 'chiuaua'}) {
#        pacienciaDuenio() / 1000
#      }
#    }
#
#    def unFoxTerrier
#      = buildDefault(builderDePerros)
#      assert unFoxTerrier.expectativaVida() == 16
#
#      def unSalchicha
#        = builderDePerros.build {
#          edad = 2
#          peso = 9
#          raza = 'salchicha'
#        }
#        assert unSalchicha.expectativaVida() == 20
#        }
#        }