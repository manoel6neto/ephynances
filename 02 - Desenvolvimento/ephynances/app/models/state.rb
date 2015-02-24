class State < ActiveRecord::Base

  validates_presence_of :name, :acronym
  validates_uniqueness_of :name, :acronym
end
