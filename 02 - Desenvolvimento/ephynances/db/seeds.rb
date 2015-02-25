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
