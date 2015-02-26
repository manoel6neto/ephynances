# encoding: UTF-8
# This file should contain all the record creation needed to seed the database with its default values.
# The data can then be loaded with the rake db:seed (or created alongside the db with db:setup).
#
# Examples:
#
#   cities = City.create([{ name: 'Chicago' }, { name: 'Copenhagen' }])
#   Mayor.create(name: 'Emanuel', city: cities.first)

######## User Levels ##############
user_levels = UserLevel.create([{ levelType: 'Administrador' }, { levelType: 'Vendedor' }, { levelType: 'Colaborador' }])

####### Admin User ##############
user = User.create(name: 'Administrador', email: 'admin@physisbrasil.com.br', phone: '7332118947', cellPhone: '7391192425', login: 'admin', status: true, cpf: '00000000000', maxSalesAmount: 0, isVerified: true, password_digest: 'Physis_2013', user_level: user_levels.first)

###### Regions #########
regions = Region.create([{ name: 'Norte', acronym: 'N' }, { name: 'Nordeste', acronym: 'NE' }, { name: 'Sudeste', acronym: 'SE' }, { name: 'Centro-Oeste', acronym: 'CO' }, { name: 'Sul', acronym: 'S' }])

##### States #########
states = State.create([{ region: regions.first, name: 'Acre', acronym: 'AC' }, { region: regions.first, name: 'Amapá', acronym: 'AP' }, { region: regions.first, name: 'Amazonas', acronym: 'AM' }, { region: regions.first, name: 'Pará', acronym: 'PA' },
                       { region: regions.first, name: 'Rondônia', acronym: 'RO' }, { region: regions.first, name: 'Roraima', acronym: 'RR' }, { region: regions.first, name: 'Tocantins', acronym: 'TO' },
                       { region: regions.second, name: 'Alagoas', acronym: 'AL' }, { region: regions.second, name: 'Bahia', acronym: 'BA' }, { region: regions.second, name: 'Ceará', acronym: 'CE' }, { region: regions.second, name: 'Maranhão', acronym: 'MA' }, { region: regions.second, name: 'Paraíba', acronym: 'PB' }, { region: regions.second, name: 'Piauí', acronym: 'PI' }, { region: regions.second, name: 'Pernambuco', acronym: 'PE' },
                       { region: regions.second, name: 'Rio Grande do Norte', acronym: 'RN' }, { region: regions.second, name: 'Sergipe', acronym: 'SE' },
                       { region: regions.third, name: 'São Paulo', acronym: 'SP' }, { region: regions.third, name: 'Minas Gerais', acronym: 'MG' }, { region: regions.third, name: 'Rio de Janeiro', acronym: 'RJ' }, { region: regions.third, name: 'Espírito Santo', acronym: 'ES' },
                       { region: regions.fourth, name: 'Goiás', acronym: 'GO' }, { region: regions.fourth, name: 'Distrito Federal', acronym: 'DF' }, { region: regions.fourth, name: 'Mato Grosso', acronym: 'MT' }, { region: regions.fourth, name: 'Mato Grosso do Sul', acronym: 'MS' },
                       { region: regions.fifth, name: 'Paraná', acronym: 'PR' }, { region: regions.fifth, name: 'Santa Catarina', acronym: 'SC' }, { region: regions.fifth, name: 'Rio Grande do Sul', acronym: 'RS' }])

###### agreement types ######
AgreementType.create(agreementType: 'Município')
AgreementType.create(agreementType: 'Parlamentar')
AgreementType.create(agreementType: 'Fundação')
AgreementType.create(agreementType: 'Consórcio')
AgreementType.create(agreementType: 'Entidades privadas sem fins lucrativos')
