class User < ActiveRecord::Base
  EMAIL_REGEXP = /\A[^@]+@([^@\.]+\.)+[^@\.]+\z/

  validates_presence_of :email, :name, :login, :status, :cpf
  validates_format_of :email, with: EMAIL_REGEXP
  validates_uniqueness_of :email, :cpf, :login
  validates_length_of :cpf, minimum: 11, maximum: 11

  has_secure_password
end
