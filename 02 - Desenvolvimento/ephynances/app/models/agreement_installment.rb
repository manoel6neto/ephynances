class AgreementInstallment < ActiveRecord::Base

  validates_presence_of :value, :status, :dueDate

  belongs_to :agreement
end
