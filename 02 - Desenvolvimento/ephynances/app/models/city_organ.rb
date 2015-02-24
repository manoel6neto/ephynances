class CityOrgan < ActiveRecord::Base

  validates_presence_of :cnpj, :cityName, :organName
  validates_uniqueness_of :cnpj
end
