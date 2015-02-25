class State < ActiveRecord::Base

  validates_presence_of :name, :acronym
  validates_uniqueness_of :name, :acronym

  has_many :city_organs
  belongs_to :region
end
