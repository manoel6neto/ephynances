# encoding: UTF-8
class AgreementDocument < ActiveRecord::Base

  validates_presence_of :name, :description, :size, :file
  validates_length_of :description, maximum: 500, allow_blank: false

  belongs_to :agreement
end
