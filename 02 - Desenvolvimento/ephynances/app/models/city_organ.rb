class CityOrgan < ActiveRecord::Base

  validates_presence_of :cnpj, :cityName, :organName
  validates_uniqueness_of :cnpj

  has_many :users
  has_many :agreements
  belongs_to :state
end
